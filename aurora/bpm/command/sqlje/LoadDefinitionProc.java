package aurora.bpm.command.sqlje;

import uncertain.composite.*;
import aurora.bpm.command.beans.*;
import java.sql.*;
import java.util.List;
import aurora.sqlje.exception.*;
import java.util.Map;
import aurora.sqlje.core.*;

public class LoadDefinitionProc implements ISqlCallEnabled {
	public String loadFromDb(String code, String version) throws Exception {
		String _$sqlje_sql_gen2 = "\n\t\t\t\t select defines \n\t\t\t\t   from bpmn_process_define \n\t\t\t\t  where process_code = ?\n\t\t\t\t\tand process_version = ?";
		PreparedStatement _$sqlje_ps_gen1 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen2);
		_$sqlje_ps_gen1.setString(1, code);
		_$sqlje_ps_gen1.setString(2, version);
		$sql.clear();
		_$sqlje_ps_gen1.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen1.getUpdateCount();
		ResultSet _$sqlje_rs_gen0 = _$sqlje_ps_gen1.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen0);
		getSqlCallStack().push(_$sqlje_ps_gen1);
		String xml = DataTransfer.transfer1(String.class, _$sqlje_rs_gen0);
		return xml;
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
