package aurora.bpm.command.sqlje;

import java.util.Arrays;
import uncertain.composite.*;
import aurora.bpm.command.beans.*;
import java.sql.*;
import java.util.List;
import aurora.sqlje.exception.*;
import java.util.Map;
import aurora.sqlje.core.*;

public class ApproveProc implements ISqlCallEnabled {
	public static final String REJECT = "REJECT";
	public static final String AGREE = "AGREE";
	public static final String WAIT = "";

	public BpmnUsertaskNode queryByRecipientRecordId(Long record_id)
			throws Exception {
		String _$sqlje_sql_gen11 = "\n\t\t\tselect * from bpmn_usertask_node\n\t\t\twhere usertask_id = (\n\t\t\t\t\tselect usertask_id from bpmn_instance_node_recipient\n\t\t\t\t\twhere record_id = ?)\n\t\t";
		PreparedStatement _$sqlje_ps_gen10 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen11);
		_$sqlje_ps_gen10.setLong(1, record_id);
		$sql.clear();
		_$sqlje_ps_gen10.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen10.getUpdateCount();
		ResultSet _$sqlje_rs_gen0 = _$sqlje_ps_gen10.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen0);
		getSqlCallStack().push(_$sqlje_ps_gen10);
		BpmnUsertaskNode bun = DataTransfer.transfer1(BpmnUsertaskNode.class,
				_$sqlje_rs_gen0);
		return bun;
	}

	public String approve(Long instance_id, Long rcpt_record_id, Long user_id,
			String action_code, String approve_content) throws Exception {
		BpmnInstanceNodeRecipient rcpt = null;
		try {
			String _$sqlje_sql_gen13 = "select * from bpmn_instance_node_recipient where record_id = ?";
			PreparedStatement _$sqlje_ps_gen12 = getSqlCallStack()
					.getCurrentConnection().prepareStatement(_$sqlje_sql_gen13);
			_$sqlje_ps_gen12.setLong(1, rcpt_record_id);
			$sql.clear();
			_$sqlje_ps_gen12.execute();
			$sql.UPDATECOUNT = _$sqlje_ps_gen12.getUpdateCount();
			ResultSet _$sqlje_rs_gen1 = _$sqlje_ps_gen12.getResultSet();
			getSqlCallStack().push(_$sqlje_rs_gen1);
			getSqlCallStack().push(_$sqlje_ps_gen12);
			rcpt = DataTransfer.transfer1(BpmnInstanceNodeRecipient.class,
					_$sqlje_rs_gen1);
		} catch (NoDataFoundException e) {
			throw new Exception("工作流审批 : 未找到代办记录:record_id:" + rcpt_record_id);
		}
		if (checkApproveValidation(rcpt_record_id, action_code, user_id) == 0) {
			throw new Exception("工作流审批 ：审批权限交验结果为'否', 工作流审批中止.");
		}
		if (!eq(rcpt.user_id, user_id)) {
			System.out.println(
					"rcpt.user_id=" + rcpt.user_id + "  user_id=" + user_id);
			throw new Exception("工作流审批 ：user_id 权限检查结果为'否',工作流审批中止");
		}
		Long usertask_id = rcpt.usertask_id;
		String _$sqlje_sql_gen15 = "select count(1) \n\t\t\tfrom bpmn_usertask_node_action \n\t\t\twhere usertask_id=? \n\t\t\tand coalesce(action_code_custom,action_code)=? ";
		PreparedStatement _$sqlje_ps_gen14 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen15);
		_$sqlje_ps_gen14.setLong(1, usertask_id);
		_$sqlje_ps_gen14.setString(2, action_code);
		$sql.clear();
		_$sqlje_ps_gen14.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen14.getUpdateCount();
		ResultSet _$sqlje_rs_gen2 = _$sqlje_ps_gen14.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen2);
		getSqlCallStack().push(_$sqlje_ps_gen14);
		Long exists = DataTransfer.transfer1(Long.class, _$sqlje_rs_gen2);
		if (exists == 0) {
			throw new Exception("工作流审批 : 不能进行指定的操作 " + action_code);
		}
		String _$sqlje_sql_gen17 = "select n.attachment_id from bpmn_instance_node_recipient n where n.record_id=?";
		PreparedStatement _$sqlje_ps_gen16 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen17);
		_$sqlje_ps_gen16.setLong(1, rcpt_record_id);
		$sql.clear();
		_$sqlje_ps_gen16.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen16.getUpdateCount();
		ResultSet _$sqlje_rs_gen3 = _$sqlje_ps_gen16.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen3);
		getSqlCallStack().push(_$sqlje_ps_gen16);
		Long attachment_id = DataTransfer.transfer1(Long.class,
				_$sqlje_rs_gen3);
		Long is_required = 0L;
		Long approve_record_id = createApproveRecord(instance_id, usertask_id,
				rcpt.seq_number, action_code, approve_content, rcpt_record_id,
				attachment_id, user_id);
		String _$sqlje_sql_gen19 = "delete from bpmn_instance_node_recipient where record_id=?";
		PreparedStatement _$sqlje_ps_gen18 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen19);
		_$sqlje_ps_gen18.setLong(1, rcpt_record_id);
		$sql.clear();
		_$sqlje_ps_gen18.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen18.getUpdateCount();
		ResultSet _$sqlje_rs_gen4 = _$sqlje_ps_gen18.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen4);
		getSqlCallStack().push(_$sqlje_ps_gen18);
		if (!in(action_code, AGREE, REJECT)) {
			System.out.println("工作流审批 : 动作为自定义" + action_code + ",直接返回.");
			return action_code;
		}
		String _$sqlje_sql_gen21 = "select * from bpmn_usertask_node where usertask_id=?";
		PreparedStatement _$sqlje_ps_gen20 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen21);
		_$sqlje_ps_gen20.setLong(1, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen20.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen20.getUpdateCount();
		ResultSet _$sqlje_rs_gen5 = _$sqlje_ps_gen20.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen5);
		getSqlCallStack().push(_$sqlje_ps_gen20);
		BpmnUsertaskNode bun = DataTransfer.transfer1(BpmnUsertaskNode.class,
				_$sqlje_rs_gen5);
		if (in(bun.approval_type, 0L, 1L, 2L, 4L, 7L)) {
			String ret = approveByApproverCount(instance_id, usertask_id, bun);
			if (!WAIT.equals(ret)) {
				String _$sqlje_sql_gen23 = "delete from bpmn_instance_node_recipient\n\t\t\t\t\twhere instance_id=? \n\t\t\t\t\t  and usertask_id=?";
				PreparedStatement _$sqlje_ps_gen22 = getSqlCallStack()
						.getCurrentConnection()
						.prepareStatement(_$sqlje_sql_gen23);
				_$sqlje_ps_gen22.setLong(1, instance_id);
				_$sqlje_ps_gen22.setLong(2, usertask_id);
				$sql.clear();
				_$sqlje_ps_gen22.execute();
				$sql.UPDATECOUNT = _$sqlje_ps_gen22.getUpdateCount();
				ResultSet _$sqlje_rs_gen6 = _$sqlje_ps_gen22.getResultSet();
				getSqlCallStack().push(_$sqlje_rs_gen6);
				getSqlCallStack().push(_$sqlje_ps_gen22);
			}
			return ret;
		}
		return WAIT;
	}

	public Long createApproveRecord(Long instance_id, Long usertask_id,
			Long seq_number, String action_code, String approve_content,
			Long rcpt_record_id, Long attachment_id, Long user_id)
					throws Exception {
		BpmnApproveRecord bar = new BpmnApproveRecord();
		bar.instance_id = instance_id;
		bar.usertask_id = usertask_id;
		bar.seq_number = seq_number;
		bar.action_token = action_code;
		bar.comment_text = approve_content;
		bar.rcpt_record_id = rcpt_record_id;
		bar.attachment_id = attachment_id;
		$sql.insert(bar);
		return bar.record_id;
	}

	/**
	 * 按人数审批<br>
	 * 
	 * @return 返回 REJECT,审批拒绝<br>
	 *         返回 AGREE,审批通过<br>
	 *         返回 "",审批尚未得出结果,需等待其他审批人
	 */
	private String approveByApproverCount(Long instance_id, Long usertask_id,
			BpmnUsertaskNode bun) throws Exception {
		String _$sqlje_sql_gen25 = "\n\t\t\t\tselect count(1) from bpmn_instance_node_hierarchy\n\t\t\t\t\twhere instance_id=?\n\t\t\t\t\tand usertask_id=?\n\t\t\t\t\tand coalesce(disabled_flag,'N')='N' ";
		PreparedStatement _$sqlje_ps_gen24 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen25);
		_$sqlje_ps_gen24.setLong(1, instance_id);
		_$sqlje_ps_gen24.setLong(2, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen24.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen24.getUpdateCount();
		ResultSet _$sqlje_rs_gen7 = _$sqlje_ps_gen24.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen7);
		getSqlCallStack().push(_$sqlje_ps_gen24);
		Long all_approver_count = DataTransfer.transfer1(Long.class,
				_$sqlje_rs_gen7);
		String _$sqlje_sql_gen27 = "\n\t\t\t\tselect count(1) from bpmn_approve_record r\n\t\t\t\t\twhere r.instance_id=?\n\t\t\t\t\tand r.usertask_id=?\n\t\t\t\t\tand r.action_token='REJECT'\n\t\t\t\t\tand coalesce(disabled_flag,'N')='N'";
		PreparedStatement _$sqlje_ps_gen26 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen27);
		_$sqlje_ps_gen26.setLong(1, instance_id);
		_$sqlje_ps_gen26.setLong(2, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen26.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen26.getUpdateCount();
		ResultSet _$sqlje_rs_gen8 = _$sqlje_ps_gen26.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen8);
		getSqlCallStack().push(_$sqlje_ps_gen26);
		Long reject_count = DataTransfer.transfer1(Long.class, _$sqlje_rs_gen8);
		String _$sqlje_sql_gen29 = "\n\t\t\t\tselect count(1) from bpmn_approve_record r\n\t\t\t\t\twhere r.instance_id=?\n\t\t\t\t\tand r.usertask_id = ?\n\t\t\t\t\tand r.action_token= 'AGREE'\n\t\t\t\t\tand coalesce(disabled_flag,'N')='N'";
		PreparedStatement _$sqlje_ps_gen28 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen29);
		_$sqlje_ps_gen28.setLong(1, instance_id);
		_$sqlje_ps_gen28.setLong(2, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen28.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen28.getUpdateCount();
		ResultSet _$sqlje_rs_gen9 = _$sqlje_ps_gen28.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen9);
		getSqlCallStack().push(_$sqlje_ps_gen28);
		Long agree_count = DataTransfer.transfer1(Long.class, _$sqlje_rs_gen9);
		Long quantity = nvl(bun.quantity, 0L);
		if (bun.approval_type == 0) {
			if (reject_count == all_approver_count)
				return REJECT;
		} else if (bun.approval_type == 1) {
			if (reject_count > 0)
				return REJECT;
		} else if (bun.approval_type == 2) {
			if (all_approver_count == 0)
				return REJECT;
			if (reject_count * 100d / all_approver_count > (100 - quantity))
				return REJECT;
			if (agree_count * 100d / all_approver_count > quantity)
				return AGREE;
		} else if (bun.approval_type == 4) {
			if (reject_count > (all_approver_count - quantity))
				return REJECT;
			if (agree_count > quantity)
				return AGREE;
		} else if (bun.approval_type == 6) {
			if (reject_count > 0 && agree_count > 0)
				System.err.println("一票通过/拒绝 数据异常");
			if (reject_count > 0)
				return REJECT;
			if (agree_count > 0)
				return AGREE;
		}
		if (reject_count == all_approver_count)
			return REJECT;
		else if (agree_count == all_approver_count)
			return AGREE;
		return WAIT;
	}

	private Long approveByRuleCount() throws Exception {
		return 1L;
	}

	public Long checkApproveValidation(Long rcpt_record_id, String action_code,
			Long user_id) throws Exception {
		return 1L;
	}

	public static boolean eq(Object o1, Object o2) {
		if (o1 == null)
			return o2 == null;
		return o1.equals(o2);
	}

	public static boolean in(Object o0, Object... args) {
		return Arrays.asList(args).contains(o0);
	}

	public static <T> T coalesce(T obj, T def) {
		if (obj == null)
			return def;
		return obj;
	}

	public static <T> T nvl(T obj, T def) {
		return coalesce(obj, def);
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
