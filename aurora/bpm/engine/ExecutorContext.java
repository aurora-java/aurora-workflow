package aurora.bpm.engine;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.mysql.jdbc.Connection;

import uncertain.composite.CompositeMap;
import uncertain.ocm.IObjectRegistry;
import aurora.bpm.command.CommandRegistry;
import aurora.bpm.command.sqlje.ProcessLogProc;
import aurora.bpm.model.DefinitionFactory;
import aurora.bpm.script.BPMScriptEngine;
import aurora.database.service.IDatabaseServiceFactory;
import aurora.plugin.script.engine.AuroraScriptEngine;
import aurora.plugin.script.scriptobject.ScriptShareObject;
import aurora.sqlje.core.IInstanceManager;
import aurora.sqlje.core.ISqlCallStack;
import aurora.sqlje.core.SqlCallStack;

public class ExecutorContext {
	private DefinitionFactory factory;
	private IInstanceManager instManger;
	private CommandRegistry cmdRegistry;
	private IObjectRegistry ior;
	private ProcessLogProc logger;
	private SqlCallStack loggerCallStack;

	public ExecutorContext(IInstanceManager instManger,
			DefinitionFactory defFactory, IObjectRegistry ior) {
		super();
		this.instManger = instManger;
		factory = defFactory;
		this.ior = ior;
		try {
			createLogger();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println(getClass().getSimpleName() + " instance created.");
	}

	private void createLogger() throws SQLException {
		IDatabaseServiceFactory dsf = (IDatabaseServiceFactory) ior
				.getInstanceOfType(IDatabaseServiceFactory.class);
		DataSource ds = dsf.getDataSource();
		java.sql.Connection conn = ds.getConnection();
		conn.setAutoCommit(false);
		loggerCallStack = new SqlCallStack(ds, conn);
		loggerCallStack.setContextData(new CompositeMap("context"));
		logger = instManger.createInstance(ProcessLogProc.class);
		logger._$setSqlCallStack(loggerCallStack);
	}

	public ProcessLogProc getLogger() {
		return logger;
	}

	public void destory() {
		try {
			loggerCallStack.cleanUp();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public IObjectRegistry getObjectRegistry() {
		return ior;
	}

	public DefinitionFactory getDefinitionFactory() {
		return factory;
	}

	public IInstanceManager getInstanceManager() {
		return instManger;
	}

	public void setCommandRegistry(CommandRegistry cr) {
		this.cmdRegistry = cr;
	}

	public CommandRegistry getCommandRegistry() {
		return cmdRegistry;
	}

	public BPMScriptEngine createScriptEngine(CompositeMap context) {
		ScriptShareObject sso = (ScriptShareObject) context
				.get(BPMScriptEngine.KEY_SSO);
		if (sso == null) {
			sso = new ScriptShareObject();
			sso.put(getObjectRegistry());
			context.put(BPMScriptEngine.KEY_SSO, sso);

		}
		BPMScriptEngine engine = new BPMScriptEngine(context);
		sso.put(engine);
		engine.registry("executorContext", this);
		engine.registry("instanceManager", instManger);
		return engine;
	}
}
