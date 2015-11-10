package aurora.bpm.command;

import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.ManualTask;

import aurora.database.service.IDatabaseServiceFactory;
import aurora.sqlje.core.ISqlCallStack;

public class ManualTaskExecutor extends AbstractCommandExecutor {
	public static final String TYPE="MANUALTASK";

	public ManualTaskExecutor(IDatabaseServiceFactory dsf) {
		super(dsf);
	}

	@Override
	public void executeWithSqlCallStack(ISqlCallStack callStack, Command cmd)
			throws Exception {
		super.executeWithSqlCallStack(callStack, cmd);
		String node_id = cmd.getOptions().getString(NODE_ID);
		org.eclipse.bpmn2.Process process = getRootProcess(loadDefinitions(cmd,
				callStack));
		FlowElementsContainer container = findFlowElementContainerById(process, cmd.getOptions().getString(SCOPE_ID));
		ManualTask mt = findFlowElementById(container, node_id, ManualTask.class);
		System.out.println("[manual task] has been done!");
		createOutgoingPath(callStack, mt, cmd);
	}

}
