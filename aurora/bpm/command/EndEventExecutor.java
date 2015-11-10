package aurora.bpm.command;

import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.SubProcess;

import aurora.bpm.command.beans.BpmnProcessInstance;
import aurora.bpm.command.sqlje.InstanceProc;
import aurora.bpm.command.sqlje.ProcessLogProc;
import aurora.database.service.IDatabaseServiceFactory;
import aurora.sqlje.core.ISqlCallStack;

public class EndEventExecutor extends AbstractCommandExecutor {

	public EndEventExecutor(IDatabaseServiceFactory dsf) {
		super(dsf);
	}

	@Override
	public void executeWithSqlCallStack(ISqlCallStack callStack, Command cmd)
			throws Exception {
		System.out.println("[End Event]" + cmd.getOptions().getString("node_id")
				+ "  reached");
		Long instance_id = cmd.getOptions().getLong(INSTANCE_ID);
		InstanceProc inst = createProc(InstanceProc.class, callStack);
		inst.finish(instance_id);
		ProcessLogProc logger = getExecutorContext().getLogger();
		logger.log(instance_id, cmd.getOptions().getLong("user_id"), "ENDEVENT",
				"process instance finished");

		org.eclipse.bpmn2.Process process = getRootProcess(
				loadDefinitions(cmd, callStack));
		FlowElementsContainer container = findFlowElementContainerById(process,
				cmd.getOptions().getString(SCOPE_ID));
		if (container == null) {
			System.err.println("scope container not found");
			return;
		}
		if (container instanceof SubProcess) {
			SubProcess sp = (SubProcess) container;
			BpmnProcessInstance bpi = inst.query(instance_id);
			BpmnProcessInstance bpi_parent = inst.query(bpi.parent_id);
			cmd.getOptions().put(INSTANCE_ID, bpi_parent.instance_id);
			cmd.getOptions().put(SCOPE_ID, bpi_parent.scope_id);
			System.out.println("sub-process instance finished,id:"+bpi.instance_id);
			

			createOutgoingPath(callStack, sp, cmd);
		}
	}

}
