package aurora.bpm.command;

import aurora.bpm.command.sqlje.ProcessLogProc;
import aurora.database.service.IDatabaseServiceFactory;

public class ExceptionLoggerExecutor extends AbstractCommandExecutor {
	public static final String TYPE = "EXCEPTION";

	public ExceptionLoggerExecutor(IDatabaseServiceFactory dsf) {
		super(dsf);
	}

	@Override
	public void execute(Command cmd) throws Exception {
		Throwable thr = (Throwable) cmd.getOptions().get("EXCEPTION");
		ProcessLogProc logger = getExecutorContext().getLogger();
		Long instance_id = cmd.getOptions().getLong(INSTANCE_ID, -1);
		Long user_id = cmd.getOptions().getLong(USER_ID, -1);
		logger.log(instance_id, user_id, "EXCEPTION", thr.getMessage());
		logger.setInstanceError(instance_id);
	}
}
