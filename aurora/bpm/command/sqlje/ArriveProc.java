package aurora.bpm.command.sqlje;

import uncertain.composite.*;
import aurora.bpm.command.beans.*;
import java.sql.*;
import java.util.List;
import aurora.sqlje.exception.*;
import java.util.Map;
import aurora.sqlje.core.*;

public class ArriveProc implements ISqlCallEnabled {
	public void arrive(Long instance_id, Long path_id, Long node_id)
			throws Exception {
	}

	protected ISqlCallStack _$sqlje_sqlCallStack = null;
	protected IInstanceManager _$sqlje_instanceManager = null;
	protected SqlFlag $sql = new SqlFlag(this);

	public ISqlCallStack getSqlCallStack() {
		return _$sqlje_sqlCallStack;
	}

	public void _$setSqlCallStack(ISqlCallStack args0) {
		_$sqlje_sqlCallStack = args0;
	}

	public IInstanceManager getInstanceManager() {
		return _$sqlje_instanceManager;
	}

	public void _$setInstanceManager(IInstanceManager args0) {
		_$sqlje_instanceManager = args0;
	}
}
