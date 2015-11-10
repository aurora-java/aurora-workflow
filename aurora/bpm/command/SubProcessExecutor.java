package aurora.bpm.command;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;

import aurora.bpm.command.sqlje.InstanceProc;
import aurora.database.service.IDatabaseServiceFactory;
import aurora.sqlje.core.ISqlCallStack;

public class SubProcessExecutor extends AbstractCommandExecutor {
	public static final String TYPE = "SUBPROCESS";

	public SubProcessExecutor(IDatabaseServiceFactory dsf) {
		super(dsf);
	}

	@Override
	public void executeWithSqlCallStack(ISqlCallStack callStack, Command cmd)
			throws Exception {
		Long parent_id = cmd.getOptions().getLong(INSTANCE_ID);
		String node_id = cmd.getOptions().getString(NODE_ID);
		org.eclipse.bpmn2.Process process = getRootProcess(
				loadDefinitions(cmd, callStack));
		FlowElementsContainer container = findFlowElementContainerById(process,
				cmd.getOptions().getString(SCOPE_ID));
		SubProcess currentNode = findFlowElementById(container, node_id,
				SubProcess.class);
		String code = cmd.getOptions().getString(PROCESS_CODE);
		String version = cmd.getOptions().getString(PROCESS_VERSION);

		InstanceProc ci = createProc(InstanceProc.class, callStack);
		Long instance_id = ci.createSubProcess(code, version, parent_id, currentNode.getId());
		System.out.println("sub-process instance created,id:"+instance_id+",parent_id:"+parent_id+",scope_id:"+currentNode.getId());
		cmd.getOptions().put(INSTANCE_ID, instance_id);// set new instance_id
		cmd.getOptions().put(SCOPE_ID, currentNode.getId());
		for (FlowElement fe : currentNode.getFlowElements()) {
			if (fe instanceof StartEvent) {
				createOutgoingPath(callStack, (StartEvent) fe, cmd);
			}
		}
	}

}
