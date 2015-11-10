package aurora.bpm.command;

import java.util.List;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.UserTask;

import aurora.bpm.command.beans.BpmnUsertaskNode;
import aurora.bpm.command.sqlje.ApproveProc;
import aurora.database.service.IDatabaseServiceFactory;
import aurora.sqlje.core.ISqlCallStack;
import uncertain.composite.CompositeMap;

public class ApproveCmdExecutor extends AbstractCommandExecutor {

	public static final String APPROVE_RESULT_PATH = "/@approve_result";

	public ApproveCmdExecutor(IDatabaseServiceFactory dsf) {
		super(dsf);
	}

	public static final String TYPE = "APPROVE";

	@Override
	public void executeWithSqlCallStack(ISqlCallStack callStack, Command cmd)
			throws Exception {
		Long instance_id = cmd.getOptions().getLong(INSTANCE_ID);
		Long user_id = cmd.getOptions().getLong(USER_ID);
		org.eclipse.bpmn2.Process process = getRootProcess(
				loadDefinitions(cmd, callStack));
		FlowElementsContainer container = findFlowElementContainerById(process,
				cmd.getOptions().getString(SCOPE_ID));

		// APPROVE 命令参数中没有node_id,仅有record_id(代办记录ID)
		String action_code = cmd.getOptions().getString("action_code");
		Long recipient_record = cmd.getOptions().getLong("record_id");
		String approve_content = cmd.getOptions().getString("approve_content");
		ApproveProc appr = createProc(ApproveProc.class, callStack);

		BpmnUsertaskNode bun = appr.queryByRecipientRecordId(recipient_record);
		String node_id = bun.node_id;
		cmd.getOptions().put(NODE_ID, node_id);
		UserTask userTask = findFlowElementById(container, node_id,
				UserTask.class);

		String result = appr.approve(instance_id, recipient_record, user_id,
				action_code, approve_content);
		if (eq(result, ApproveProc.WAIT)) {
			System.out.printf(
					"[usertask]%s approve not complete yet,wait ...\n",
					node_id);
			return;
		}
		// approve complete,the approve result can be accessed by
		// $ctx.approve_result
		callStack.getContextData().putObject(APPROVE_RESULT_PATH, result, true);

		gotoNext(userTask, callStack, cmd, container, node_id, result);

	}

	protected void gotoNext(UserTask userTask, ISqlCallStack callStack,
			Command cmd, FlowElementsContainer container, String node_id,
			String result) throws Exception {
		// List<SequenceFlow> outgoings = userTask.getOutgoing();
		// // 如果下一个节点是选择网管,则交由选择网管进行判定
		// if (outgoings.size() == 1
		// && outgoings.get(0).getTargetRef() instanceof ExclusiveGateway) {
		// System.out.printf("[usertask]%s decision will be made by <%s>\n",
		// node_id, outgoings.get(0).getTargetRef().getId());
		// createPath(callStack, outgoings.get(0), cmd);
		// return;
		// }

		if (!eq(result, ApproveProc.REJECT)) {
			System.out.printf("[usertask]%s approve %s\n", node_id, result);
			createOutgoingPath(callStack, userTask, cmd);
		} else {
			System.out.printf("[usertask]%s approve REJECT, goto End-Event\n",
					node_id);
			for (FlowElement fe : container.getFlowElements()) {
				if (fe instanceof EndEvent) {
					CompositeMap opts = cloneOptions(cmd);
					opts.put(NODE_ID, fe.getId());
					Command cmd2 = new Command("ENDEVENT", opts);
					dispatchCommand(callStack, cmd2);
					return;
				}
			}
		}
	}

}
