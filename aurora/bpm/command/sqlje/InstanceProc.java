package aurora.bpm.command.sqlje;

import uncertain.composite.*;
import aurora.bpm.command.sqlje.*;
import aurora.bpm.command.beans.*;
import java.sql.*;
import java.util.List;
import aurora.sqlje.exception.*;
import java.util.Map;
import aurora.sqlje.core.*;

public class InstanceProc implements ISqlCallEnabled {
	public Long create(String code, String version, Long parent_id,
			String scope_id, Long instance_param) throws Exception {
		String _$sqlje_sql_gen12 = "select description from bpmn_process_define d where d.process_code = ? and d.process_version=?";
		PreparedStatement _$sqlje_ps_gen11 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen12);
		_$sqlje_ps_gen11.setString(1, code);
		_$sqlje_ps_gen11.setString(2, version);
		$sql.clear();
		_$sqlje_ps_gen11.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen11.getUpdateCount();
		ResultSet _$sqlje_rs_gen0 = _$sqlje_ps_gen11.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen0);
		getSqlCallStack().push(_$sqlje_ps_gen11);
		String desc = DataTransfer.transfer1(String.class, _$sqlje_rs_gen0);
		BpmnProcessInstance bpi = new BpmnProcessInstance();
		bpi.status = "RUNNING";
		bpi.process_code = code;
		bpi.process_version = version;
		bpi.scope_id = scope_id;
		bpi.parent_id = parent_id;
		bpi.instance_param = instance_param;
		bpi.description = desc;
		$sql.insert(bpi);
		BpmnDocumentReference doc_ref = null;
		String data_object = "{}";
		try {
			String _$sqlje_sql_gen14 = "select df.*\n\t\t\t\tfrom bpmn_document_reference df,bpmn_process_define pd\n\t\t\t\twhere df.category_id = pd.category_id\n\t\t\t\tand pd.process_code=?\n\t\t\t\tand pd.process_version=?";
			PreparedStatement _$sqlje_ps_gen13 = getSqlCallStack()
					.getCurrentConnection().prepareStatement(_$sqlje_sql_gen14);
			_$sqlje_ps_gen13.setString(1, code);
			_$sqlje_ps_gen13.setString(2, version);
			$sql.clear();
			_$sqlje_ps_gen13.execute();
			$sql.UPDATECOUNT = _$sqlje_ps_gen13.getUpdateCount();
			ResultSet _$sqlje_rs_gen1 = _$sqlje_ps_gen13.getResultSet();
			getSqlCallStack().push(_$sqlje_rs_gen1);
			getSqlCallStack().push(_$sqlje_ps_gen13);
			doc_ref = DataTransfer.transfer1(BpmnDocumentReference.class,
					_$sqlje_rs_gen1);
			StringBuilder _$sqlje_sql_gen16 = new StringBuilder();
			_$sqlje_sql_gen16.append("select * from ");
			_$sqlje_sql_gen16.append("(" + doc_ref.ref_detail + ")");
			_$sqlje_sql_gen16.append(" z\n\t\t\t\t\t\t\t\t\t\t\twhere ");
			_$sqlje_sql_gen16.append(doc_ref.ref_id_column_name);
			_$sqlje_sql_gen16.append(" = ?");
			String _$sqlje_sql_gen17 = _$sqlje_sql_gen16.toString();
			PreparedStatement _$sqlje_ps_gen15 = getSqlCallStack()
					.getCurrentConnection().prepareStatement(_$sqlje_sql_gen17);
			_$sqlje_ps_gen15.setLong(1, instance_param);
			$sql.clear();
			_$sqlje_ps_gen15.execute();
			$sql.UPDATECOUNT = _$sqlje_ps_gen15.getUpdateCount();
			ResultSet _$sqlje_rs_gen2 = _$sqlje_ps_gen15.getResultSet();
			getSqlCallStack().push(_$sqlje_rs_gen2);
			getSqlCallStack().push(_$sqlje_ps_gen15);
			CompositeMap process_param = DataTransfer
					.transfer1(CompositeMap.class, _$sqlje_rs_gen2);
			data_object = JSONAdaptor
					.toJSONObject(process_param).toString();
		} catch (NoDataFoundException e) {
		}
		BpmnProcessData data = new BpmnProcessData();
		data.instance_id = bpi.instance_id;
		data.data_object = data_object;
		$sql.insert(data);
		return bpi.instance_id;
	}

	/**
	 * 创建子流程<br>
	 * instance_param 无需指定,继承自父流程<br>
	 * data_object 继承自父流程
	 */
	public Long createSubProcess(String code, String version, Long parent_id,
			String scope_id) throws Exception {
		String _$sqlje_sql_gen19 = "select description from bpmn_process_define d where d.process_code = ? and d.process_version=?";
		PreparedStatement _$sqlje_ps_gen18 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen19);
		_$sqlje_ps_gen18.setString(1, code);
		_$sqlje_ps_gen18.setString(2, version);
		$sql.clear();
		_$sqlje_ps_gen18.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen18.getUpdateCount();
		ResultSet _$sqlje_rs_gen3 = _$sqlje_ps_gen18.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen3);
		getSqlCallStack().push(_$sqlje_ps_gen18);
		String desc = DataTransfer.transfer1(String.class, _$sqlje_rs_gen3);
		String _$sqlje_sql_gen21 = "select instance_param from bpmn_process_instance where instance_id = ?";
		PreparedStatement _$sqlje_ps_gen20 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen21);
		_$sqlje_ps_gen20.setLong(1, parent_id);
		$sql.clear();
		_$sqlje_ps_gen20.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen20.getUpdateCount();
		ResultSet _$sqlje_rs_gen4 = _$sqlje_ps_gen20.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen4);
		getSqlCallStack().push(_$sqlje_ps_gen20);
		Long instance_param = DataTransfer.transfer1(Long.class,
				_$sqlje_rs_gen4);
		BpmnProcessInstance bpi = new BpmnProcessInstance();
		bpi.status = "RUNNING";
		bpi.process_code = code;
		bpi.process_version = version;
		bpi.scope_id = scope_id;
		bpi.parent_id = parent_id;
		bpi.instance_param = instance_param;
		bpi.description = desc;
		$sql.insert(bpi);
		BpmnDocumentReference doc_ref = null;
		String _$sqlje_sql_gen23 = "select d.data_object from bpmn_process_data d where d.instance_id=?";
		PreparedStatement _$sqlje_ps_gen22 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen23);
		_$sqlje_ps_gen22.setLong(1, parent_id);
		$sql.clear();
		_$sqlje_ps_gen22.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen22.getUpdateCount();
		ResultSet _$sqlje_rs_gen5 = _$sqlje_ps_gen22.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen5);
		getSqlCallStack().push(_$sqlje_ps_gen22);
		String data_object = DataTransfer.transfer1(String.class,
				_$sqlje_rs_gen5);
		BpmnProcessData data = new BpmnProcessData();
		data.instance_id = bpi.instance_id;
		data.data_object = data_object;
		$sql.insert(data);
		return bpi.instance_id;
	}

	public BpmnProcessInstance query(Long instance_id) throws Exception {
		String _$sqlje_sql_gen25 = "select * from bpmn_process_instance where instance_id=?";
		PreparedStatement _$sqlje_ps_gen24 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen25);
		_$sqlje_ps_gen24.setLong(1, instance_id);
		$sql.clear();
		_$sqlje_ps_gen24.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen24.getUpdateCount();
		ResultSet _$sqlje_rs_gen6 = _$sqlje_ps_gen24.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen6);
		getSqlCallStack().push(_$sqlje_ps_gen24);
		BpmnProcessInstance bpi = DataTransfer
				.transfer1(BpmnProcessInstance.class, _$sqlje_rs_gen6);
		return bpi;
	}

	public BpmnProcessData getProcessData(Long instance_id) throws Exception {
		String _$sqlje_sql_gen27 = "select * from bpmn_process_data where instance_id = ?";
		PreparedStatement _$sqlje_ps_gen26 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen27);
		_$sqlje_ps_gen26.setLong(1, instance_id);
		$sql.clear();
		_$sqlje_ps_gen26.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen26.getUpdateCount();
		ResultSet _$sqlje_rs_gen7 = _$sqlje_ps_gen26.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen7);
		getSqlCallStack().push(_$sqlje_ps_gen26);
		BpmnProcessData data = DataTransfer.transfer1(BpmnProcessData.class,
				_$sqlje_rs_gen7);
		return data;
	}

	public void saveDataObject(BpmnProcessData data) throws Exception {
		String _$sqlje_sql_gen29 = "update bpmn_process_data set data_object = ? where instance_id = ?";
		PreparedStatement _$sqlje_ps_gen28 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen29);
		_$sqlje_ps_gen28.setObject(1, data.data_object);
		_$sqlje_ps_gen28.setObject(2, data.instance_id);
		$sql.clear();
		_$sqlje_ps_gen28.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen28.getUpdateCount();
		ResultSet _$sqlje_rs_gen8 = _$sqlje_ps_gen28.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen8);
		getSqlCallStack().push(_$sqlje_ps_gen28);
	}

	public void finish(Long instance_id) throws Exception {
		String _$sqlje_sql_gen31 = "update bpmn_process_instance set status='FINISH' where instance_id=?";
		PreparedStatement _$sqlje_ps_gen30 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen31);
		_$sqlje_ps_gen30.setLong(1, instance_id);
		$sql.clear();
		_$sqlje_ps_gen30.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen30.getUpdateCount();
		ResultSet _$sqlje_rs_gen9 = _$sqlje_ps_gen30.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen9);
		getSqlCallStack().push(_$sqlje_ps_gen30);
		String _$sqlje_sql_gen33 = "delete from bpmn_process_token where instance_id=?";
		PreparedStatement _$sqlje_ps_gen32 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen33);
		_$sqlje_ps_gen32.setLong(1, instance_id);
		$sql.clear();
		_$sqlje_ps_gen32.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen32.getUpdateCount();
		ResultSet _$sqlje_rs_gen10 = _$sqlje_ps_gen32.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen10);
		getSqlCallStack().push(_$sqlje_ps_gen32);
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
