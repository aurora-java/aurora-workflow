package aurora.bpm.command.sqlje;

import uncertain.composite.*;
import aurora.bpm.command.sqlje.*;
import java.util.*;
import aurora.bpm.script.*;
import aurora.bpm.command.beans.*;
import java.sql.*;
import java.util.List;
import aurora.sqlje.exception.*;
import java.util.Map;
import aurora.sqlje.core.*;

public class UserTaskProc implements aurora.sqlje.core.ISqlCallEnabled {
	public BpmnUsertaskNode query(String code, String version, String node_id)
			throws Exception {
		try {
			String _$sqlje_sql_gen25 = "select * from bpmn_usertask_node where process_code=? and process_version=? and node_id = ?";
			PreparedStatement _$sqlje_ps_gen24 = getSqlCallStack()
					.getCurrentConnection().prepareStatement(_$sqlje_sql_gen25);
			_$sqlje_ps_gen24.setString(1, code);
			_$sqlje_ps_gen24.setString(2, version);
			_$sqlje_ps_gen24.setString(3, node_id);
			$sql.clear();
			_$sqlje_ps_gen24.execute();
			$sql.UPDATECOUNT = _$sqlje_ps_gen24.getUpdateCount();
			ResultSet _$sqlje_rs_gen0 = _$sqlje_ps_gen24.getResultSet();
			getSqlCallStack().push(_$sqlje_rs_gen0);
			getSqlCallStack().push(_$sqlje_ps_gen24);
			BpmnUsertaskNode ut = DataTransfer.transfer1(
					BpmnUsertaskNode.class, _$sqlje_rs_gen0);
			return ut;
		} catch (NoDataFoundException e) {
		}
		return null;
	}

	public List<BpmnNodeRecipientSet> getRecipientSet(Long usertask_id)
			throws Exception {
		String _$sqlje_sql_gen27 = "select * from bpmn_node_recipient_set where usertask_id = ? order by recipient_sequence";
		PreparedStatement _$sqlje_ps_gen26 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen27);
		_$sqlje_ps_gen26.setLong(1, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen26.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen26.getUpdateCount();
		ResultSet _$sqlje_rs_gen1 = _$sqlje_ps_gen26.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen1);
		getSqlCallStack().push(_$sqlje_ps_gen26);
		List<BpmnNodeRecipientSet> list = DataTransfer.transferAll(List.class,
				BpmnNodeRecipientSet.class, _$sqlje_rs_gen1);
		return list;
	}

	public BpmnRecipientRules getRecipientRule(String rule_code)
			throws Exception {
		String _$sqlje_sql_gen29 = "select * from bpmn_recipient_rules where rule_code = ?";
		PreparedStatement _$sqlje_ps_gen28 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen29);
		_$sqlje_ps_gen28.setString(1, rule_code);
		$sql.clear();
		_$sqlje_ps_gen28.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen28.getUpdateCount();
		ResultSet _$sqlje_rs_gen2 = _$sqlje_ps_gen28.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen2);
		getSqlCallStack().push(_$sqlje_ps_gen28);
		BpmnRecipientRules rule = DataTransfer.transfer1(
				BpmnRecipientRules.class, _$sqlje_rs_gen2);
		return rule;
	}

	void log(Object obj) {
		System.out.println(obj);
	}

	/**
	 * 创建审批规则
	 */
	public void createInstanceNodeRule(Long instance_id, Long usertask_id,
			Long user_id) throws Exception {
		String _$sqlje_sql_gen31 = "select * from bpmn_node_recipient_set where usertask_id=?";
		PreparedStatement _$sqlje_ps_gen30 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen31);
		_$sqlje_ps_gen30.setLong(1, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen30.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen30.getUpdateCount();
		ResultSet _$sqlje_rs_gen3 = _$sqlje_ps_gen30.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen3);
		getSqlCallStack().push(_$sqlje_ps_gen30);
		for (BpmnNodeRecipientSet node_recipient : new ResultSetIterator<BpmnNodeRecipientSet>(
				_$sqlje_rs_gen3, BpmnNodeRecipientSet.class)) {
			insertBpmInstanceNodeRule(instance_id, usertask_id, "NODE",
					node_recipient.recipient_set_id, node_recipient.rule_code,
					node_recipient.rule_sequence,
					node_recipient.recipient_sequence,
					node_recipient.parameter_1_value,
					node_recipient.parameter_2_value,
					node_recipient.parameter_3_value,
					node_recipient.parameter_4_value, "RECIPIENT_RULE", user_id);
		}
	}

	public Long insertBpmInstanceNodeRule(Long instance_id, Long usertask_id,
			String recipient_type, Long recipient_set_id, String rule_code,
			Long rule_sequence, Long recipient_sequence, String param1,
			String param2, String param3, String param4, String rule_type,
			Long user_id) throws Exception {
		BpmnInstanceNodeRule rule = new BpmnInstanceNodeRule();
		rule.instance_id = instance_id;
		rule.usertask_id = usertask_id;
		rule.recipient_type = recipient_type;
		rule.rule_code = rule_code;
		rule.rule_sequence = rule_sequence;
		rule.recipient_sequence = recipient_sequence;
		rule.parameter_1_value = param1;
		rule.parameter_2_value = param2;
		rule.parameter_3_value = param3;
		rule.parameter_4_value = param4;
		rule.rule_type = rule_type;
		$sql.insert(rule);
		return rule.rule_record_id;
	}

	/**
	 * 创建审批层次
	 */
	public void createInstanceNodeHierarchy(Long instance_id, Long usertask_id,
			Long user_id) throws Exception {
		String _$sqlje_sql_gen33 = "select * from bpmn_instance_node_rule \n\t\t\twhere instance_id = ? and usertask_id=?\n\t\t    \t\t\tand rule_type = 'RECIPIENT_RULE' order by rule_sequence ";
		PreparedStatement _$sqlje_ps_gen32 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen33);
		_$sqlje_ps_gen32.setLong(1, instance_id);
		_$sqlje_ps_gen32.setLong(2, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen32.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen32.getUpdateCount();
		ResultSet _$sqlje_rs_gen4 = _$sqlje_ps_gen32.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen4);
		getSqlCallStack().push(_$sqlje_ps_gen32);
		for (BpmnInstanceNodeRule recipient_rule : new ResultSetIterator<BpmnInstanceNodeRule>(
				_$sqlje_rs_gen4, BpmnInstanceNodeRule.class)) {
			if (eq(recipient_rule.rule_code, "SCRIPT")) {
				System.out.println("####will execute script here#####");
			} else {
				String proc_name = null;
				if (eq(recipient_rule.rule_code, "SQLJE"))
					proc_name = recipient_rule.parameter_1_value;
				else {
					String _$sqlje_sql_gen35 = "select procedure_name from bpmn_recipient_rules where rule_code = ?";
					PreparedStatement _$sqlje_ps_gen34 = getSqlCallStack()
							.getCurrentConnection().prepareStatement(
									_$sqlje_sql_gen35);
					_$sqlje_ps_gen34.setObject(1, recipient_rule.rule_code);
					$sql.clear();
					_$sqlje_ps_gen34.execute();
					$sql.UPDATECOUNT = _$sqlje_ps_gen34.getUpdateCount();
					ResultSet _$sqlje_rs_gen5 = _$sqlje_ps_gen34.getResultSet();
					getSqlCallStack().push(_$sqlje_rs_gen5);
					getSqlCallStack().push(_$sqlje_ps_gen34);
					proc_name = DataTransfer.transfer1(String.class,
							_$sqlje_rs_gen5);
				}
				int idx = proc_name.lastIndexOf('.');
				String proc_class = proc_name.substring(0, idx);
				String proc_method = proc_name.substring(idx + 1);
				ISqlCallEnabled proc = getInstanceManager().createInstance(
						proc_class, this);
				java.lang.reflect.Method m = proc.getClass().getMethod(
						proc_method, String.class, String.class, String.class,
						String.class, Long.class);
				if (proc != null && m != null) {
					System.out.println("execute proc:" + proc_name);
					m.invoke(proc, recipient_rule.parameter_1_value,
							recipient_rule.parameter_2_value,
							recipient_rule.parameter_3_value,
							recipient_rule.parameter_4_value,
							recipient_rule.rule_record_id);
				}
			}
		}
		String _$sqlje_sql_gen37 = "select approver_id,\n\t\t\t\t\t\t\t\t\t\t\t            min(t.hierarchy_record_id) as hierarchy_record_id\n\t\t\t\t\t\t\t\t\t\t\t       from bpmn_instance_node_hierarchy t\n\t\t\t\t\t\t\t\t\t\t\t      where t.instance_id = ?\n\t\t\t\t\t\t\t\t\t\t\t        and t.usertask_id = ?\n\t\t\t\t\t\t\t\t\t\t\t      group by approver_id\n\t\t\t\t\t\t\t\t\t\t\t     having count(*) > 1";
		PreparedStatement _$sqlje_ps_gen36 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen37);
		_$sqlje_ps_gen36.setLong(1, instance_id);
		_$sqlje_ps_gen36.setLong(2, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen36.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen36.getUpdateCount();
		ResultSet _$sqlje_rs_gen6 = _$sqlje_ps_gen36.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen6);
		getSqlCallStack().push(_$sqlje_ps_gen36);
		for (BpmnInstanceNodeHierarchy repeat_rcpt : new ResultSetIterator<BpmnInstanceNodeHierarchy>(
				_$sqlje_rs_gen6, BpmnInstanceNodeHierarchy.class)) {
			String _$sqlje_sql_gen39 = "update bpmn_instance_node_hierarchy\n\t\t         set disabled_flag    = 'Y',\n\t\t             note             = 'Repeated Recipient Rule',\n\t\t             last_update_date = CURRENT_TIMESTAMP,\n\t\t             last_updated_by  = ?\n\t\t       where instance_id = ?\n\t\t         and usertask_id = ?\n\t\t         and approver_id = repeat_rcpt.approver_id\n\t\t         and hierarchy_record_id > repeat_rcpt.hierarchy_record_id";
			PreparedStatement _$sqlje_ps_gen38 = getSqlCallStack()
					.getCurrentConnection().prepareStatement(_$sqlje_sql_gen39);
			_$sqlje_ps_gen38.setLong(1, user_id);
			_$sqlje_ps_gen38.setLong(2, instance_id);
			_$sqlje_ps_gen38.setLong(3, usertask_id);
			$sql.clear();
			_$sqlje_ps_gen38.execute();
			$sql.UPDATECOUNT = _$sqlje_ps_gen38.getUpdateCount();
			ResultSet _$sqlje_rs_gen7 = _$sqlje_ps_gen38.getResultSet();
			getSqlCallStack().push(_$sqlje_rs_gen7);
			getSqlCallStack().push(_$sqlje_ps_gen38);
		}
	}

	/**
	 * 创建审批人(代办)
	 */
	public void createInstanceNodeRecipient(Long instance_id, Long usertask_id,
			Long user_id) throws Exception {
		String _$sqlje_sql_gen41 = "select * from bpmn_usertask_node where usertask_id = ?";
		PreparedStatement _$sqlje_ps_gen40 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen41);
		_$sqlje_ps_gen40.setLong(1, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen40.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen40.getUpdateCount();
		ResultSet _$sqlje_rs_gen8 = _$sqlje_ps_gen40.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen8);
		getSqlCallStack().push(_$sqlje_ps_gen40);
		CompositeMap current_node = DataTransfer.transfer1(CompositeMap.class,
				_$sqlje_rs_gen8);
		Long current_node_id = usertask_id;
		Long can_no_approver = current_node.getLong("can_no_approver", 0);
		String _$sqlje_sql_gen43 = "select count(*) from bpmn_instance_node_hierarchy\n\t\t\twhere instance_id=?\n\t\t\tand usertask_id = ?\n\t\t\tand coalesce(posted_flag,'N')='N'\n\t\t\tand coalesce(disabled_flag,'N')='N'";
		PreparedStatement _$sqlje_ps_gen42 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen43);
		_$sqlje_ps_gen42.setLong(1, instance_id);
		_$sqlje_ps_gen42.setLong(2, current_node_id);
		$sql.clear();
		_$sqlje_ps_gen42.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen42.getUpdateCount();
		ResultSet _$sqlje_rs_gen9 = _$sqlje_ps_gen42.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen9);
		getSqlCallStack().push(_$sqlje_ps_gen42);
		Long count = DataTransfer.transfer1(Long.class, _$sqlje_rs_gen9);
		if (count == 0 && can_no_approver == 0) {
			throw new Exception(" no approver found ");
		}
		String _$sqlje_sql_gen45 = "select a.hierarchy_record_id\n\t\t\t\t\t\t\t\tfrom bpmn_instance_node_hierarchy a\n\t\t\t\t\t\t\t\twhere a.instance_id = ?\n\t\t\t\t\t\t\t\tand a.usertask_id = ?\n\t\t\t\t\t\t\t\tand a.rule_detail_id is null";
		PreparedStatement _$sqlje_ps_gen44 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen45);
		_$sqlje_ps_gen44.setLong(1, instance_id);
		_$sqlje_ps_gen44.setLong(2, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen44.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen44.getUpdateCount();
		ResultSet _$sqlje_rs_gen10 = _$sqlje_ps_gen44.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen10);
		getSqlCallStack().push(_$sqlje_ps_gen44);
		for (Long root_approver : new ResultSetIterator<Long>(_$sqlje_rs_gen10,
				Long.class)) {
			String _$sqlje_sql_gen47 = "select a.rule_record_id,\n\t\t\t\t\t\t\t\t\t\tmin(a.seq_number) as seq_number\n\t\t\t\t\t\t\t\t\tfrom bpmn_instance_node_hierarchy a\n\t\t\t\t\t\t\t\t\twhere a.instance_id=?\n\t\t\t\t\t\t\t\t\tand a.usertask_id =?\n\t\t\t\t\t\t\t\t\tand coalesce(posted_flag,'N')='N'\n\t\t\t\t\t\t\t\t\tand coalesce(disabled_flag,'N')='N'\n\t\t\t\t\t\t\t\t\tgroup by a.rule_record_id";
			PreparedStatement _$sqlje_ps_gen46 = getSqlCallStack()
					.getCurrentConnection().prepareStatement(_$sqlje_sql_gen47);
			_$sqlje_ps_gen46.setLong(1, instance_id);
			_$sqlje_ps_gen46.setLong(2, usertask_id);
			$sql.clear();
			_$sqlje_ps_gen46.execute();
			$sql.UPDATECOUNT = _$sqlje_ps_gen46.getUpdateCount();
			ResultSet _$sqlje_rs_gen11 = _$sqlje_ps_gen46.getResultSet();
			getSqlCallStack().push(_$sqlje_rs_gen11);
			getSqlCallStack().push(_$sqlje_ps_gen46);
			for (Map approver_seq : new ResultSetIterator<Map>(
					_$sqlje_rs_gen11, Map.class)) {
				Long rule_record_id = ResultSetUtil.get(approver_seq,
						"rule_record_id", Long.class);
				Long seq_number = ResultSetUtil.get(approver_seq, "seq_number",
						Long.class);
				Long root_hierarchy_id = root_approver;
				String _$sqlje_sql_gen49 = "select count(*)\n\t\t\t\t\tfrom bpmn_instance_node_hierarchy a,bpmn_instance_node_recipient b\n\t\t\t\t\twhere a.hierarchy_record_id=b.hierarchy_record_id\n\t\t\t\t\tand a.instance_id = ?\n\t\t\t\t\tand a.usertask_id = ?\n\t\t\t\t\tand a.rule_record_id = ?\n\t\t\t\t\tand a.hierarchy_record_id in(\n\t\t\t\t\t\t\tselect a.hierarchy_record_id\n\t\t\t\t\t\t\tfrom bpmn_instance_node_hierarchy a\n\t\t\t\t\t\t\twhere a.instance_id=?\n\t\t\t\t\t\t\tand a.usertask_id=?\n\t\t\t\t\t\t\tand coalesce(posted_flag,'N')='N'\n\t\t\t\t\t\t\tand coalesce(disabled_flag,'N')='N')\n\t\t\t\t\tand a.seq_number<?";
				PreparedStatement _$sqlje_ps_gen48 = getSqlCallStack()
						.getCurrentConnection().prepareStatement(
								_$sqlje_sql_gen49);
				_$sqlje_ps_gen48.setLong(1, instance_id);
				_$sqlje_ps_gen48.setLong(2, usertask_id);
				_$sqlje_ps_gen48.setLong(3, rule_record_id);
				_$sqlje_ps_gen48.setLong(4, instance_id);
				_$sqlje_ps_gen48.setLong(5, usertask_id);
				_$sqlje_ps_gen48.setLong(6, seq_number);
				$sql.clear();
				_$sqlje_ps_gen48.execute();
				$sql.UPDATECOUNT = _$sqlje_ps_gen48.getUpdateCount();
				ResultSet _$sqlje_rs_gen12 = _$sqlje_ps_gen48.getResultSet();
				getSqlCallStack().push(_$sqlje_rs_gen12);
				getSqlCallStack().push(_$sqlje_ps_gen48);
				count = DataTransfer.transfer1(Long.class, _$sqlje_rs_gen12);
				if (count == 0) {
					String _$sqlje_sql_gen51 = "\n\t\t\t\t\t\t select a.approver_id, a.hierarchy_record_id\n\t\t\t\t\t        from bpmn_instance_node_hierarchy a\n\t\t\t\t\t       where instance_id = ?\n\t\t\t\t\t         and usertask_id = ?\n\t\t\t\t\t         and seq_number = ?\n\t\t\t\t\t         and (rule_record_id = ? or\n\t\t\t\t\t             (rule_record_id is null and ? is null))\n\t\t\t\t\t         and hierarchy_record_id in\n\t\t\t\t\t             (select a.hierarchy_record_id\n\t\t\t\t\t                from bpmn_instance_node_hierarchy a\n\t\t\t\t\t               where a.instance_id = ?\n\t\t\t\t\t                 and a.usertask_id = ?\n\t\t\t\t\t                 and coalesce(posted_flag, 'N') = 'N'\n\t\t\t\t\t                 and coalesce(disabled_flag, 'N') = 'N')\n\t\t\t\t\t         and coalesce(posted_flag, 'N') = 'N'\n\t\t\t\t\t         and coalesce(disabled_flag, 'N') = 'N'";
					_$sqlje_sql_gen51 = $sql
							._$prepareLimitSql(_$sqlje_sql_gen51);
					PreparedStatement _$sqlje_ps_gen50 = getSqlCallStack()
							.getCurrentConnection().prepareStatement(
									_$sqlje_sql_gen51);
					_$sqlje_ps_gen50.setLong(1, instance_id);
					_$sqlje_ps_gen50.setLong(2, current_node_id);
					_$sqlje_ps_gen50.setLong(3, seq_number);
					_$sqlje_ps_gen50.setLong(4, rule_record_id);
					_$sqlje_ps_gen50.setLong(5, rule_record_id);
					_$sqlje_ps_gen50.setLong(6, instance_id);
					_$sqlje_ps_gen50.setLong(7, usertask_id);
					$sql._$prepareLimitParaBinding(_$sqlje_ps_gen50, 0, 1, 8);
					$sql.clear();
					_$sqlje_ps_gen50.execute();
					$sql.UPDATECOUNT = _$sqlje_ps_gen50.getUpdateCount();
					ResultSet _$sqlje_rs_gen13 = _$sqlje_ps_gen50
							.getResultSet();
					getSqlCallStack().push(_$sqlje_rs_gen13);
					getSqlCallStack().push(_$sqlje_ps_gen50);
					CompositeMap un_post_user = DataTransfer.transfer1(
							CompositeMap.class, _$sqlje_rs_gen13);
					Long approver_id = un_post_user.getLong("approver_id");
					Long hierarchy_record_id = un_post_user
							.getLong("hierarchy_record_id");
					String _$sqlje_sql_gen53 = "update bpmn_instance_node_hierarchy\n\t\t\t\t\t\tset posted_flag='Y',\n\t\t\t\t\t\tlast_update_date=current_timestamp,\n\t\t\t\t\t\tlast_updated_by=1\n\t\t\t\t\t\twhere hierarchy_record_id=?";
					PreparedStatement _$sqlje_ps_gen52 = getSqlCallStack()
							.getCurrentConnection().prepareStatement(
									_$sqlje_sql_gen53);
					_$sqlje_ps_gen52.setLong(1, hierarchy_record_id);
					$sql.clear();
					_$sqlje_ps_gen52.execute();
					$sql.UPDATECOUNT = _$sqlje_ps_gen52.getUpdateCount();
					ResultSet _$sqlje_rs_gen14 = _$sqlje_ps_gen52
							.getResultSet();
					getSqlCallStack().push(_$sqlje_rs_gen14);
					getSqlCallStack().push(_$sqlje_ps_gen52);
					createNodeRecipient(instance_id, current_node_id,
							seq_number, approver_id, hierarchy_record_id,
							user_id);
				}
			}
		}
	}

	public Long createNodeRecipient(Long instance_id, Long usertask_id,
			Long seq_number, Long recipient_id, Long hierarchy_record_id,
			Long user_id) throws Exception {
		String _$sqlje_sql_gen55 = "select * from bpmn_usertask_node where usertask_id=?";
		PreparedStatement _$sqlje_ps_gen54 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen55);
		_$sqlje_ps_gen54.setLong(1, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen54.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen54.getUpdateCount();
		ResultSet _$sqlje_rs_gen15 = _$sqlje_ps_gen54.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen15);
		getSqlCallStack().push(_$sqlje_ps_gen54);
		BpmnUsertaskNode node = DataTransfer.transfer1(BpmnUsertaskNode.class,
				_$sqlje_rs_gen15);
		java.sql.Date date_limit = null;
		if (eq(node.is_date_limited, 1L)) {
		}
		BpmnInstanceNodeRecipient rcpt = new BpmnInstanceNodeRecipient();
		rcpt.instance_id = instance_id;
		rcpt.usertask_id = usertask_id;
		rcpt.seq_number = seq_number;
		rcpt.user_id = recipient_id;
		rcpt.date_limit = date_limit;
		rcpt.hierarchy_record_id = hierarchy_record_id;
		$sql.insert(rcpt);
		Long record_id = rcpt.record_id;
		return record_id;
	}

	public boolean autoApprove(Long instance_id, Long usertask_id, Long user_id)
			throws Exception {
		String _$sqlje_sql_gen57 = "\n\t\t\tselect r.*, n.can_auto_pass, n.is_self_re_commit\n            from bpmn_instance_node_recipient r,\n                 bpmn_usertask_node           n\n           where instance_id = ?\n             and n.usertask_id = r.usertask_id\n             and (n.can_auto_pass = 1 or n.is_self_re_commit = 1)\n\t\t";
		PreparedStatement _$sqlje_ps_gen56 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen57);
		_$sqlje_ps_gen56.setLong(1, instance_id);
		$sql.clear();
		_$sqlje_ps_gen56.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen56.getUpdateCount();
		ResultSet _$sqlje_rs_gen16 = _$sqlje_ps_gen56.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen16);
		getSqlCallStack().push(_$sqlje_ps_gen56);
		for (CompositeMap recipient : new ResultSetIterator<CompositeMap>(
				_$sqlje_rs_gen16, CompositeMap.class)) {
			String auto_approve_flag = "N";
			Long can_auto_pass = recipient.getLong("can_auto_pass");
			Long record_id = recipient.getLong("record_id");
			if (can_auto_pass == 1) {
				try {
					String _$sqlje_sql_gen59 = "select * from bpmn_instance_node_recipient \n\t\t\t\t\twhere instance_id = ? \n\t\t\t\t\tand record_id = ?";
					PreparedStatement _$sqlje_ps_gen58 = getSqlCallStack()
							.getCurrentConnection().prepareStatement(
									_$sqlje_sql_gen59);
					_$sqlje_ps_gen58.setLong(1, instance_id);
					_$sqlje_ps_gen58.setLong(2, record_id);
					$sql.clear();
					_$sqlje_ps_gen58.execute();
					$sql.UPDATECOUNT = _$sqlje_ps_gen58.getUpdateCount();
					ResultSet _$sqlje_rs_gen17 = _$sqlje_ps_gen58
							.getResultSet();
					getSqlCallStack().push(_$sqlje_rs_gen17);
					getSqlCallStack().push(_$sqlje_ps_gen58);
					BpmnInstanceNodeRecipient binc = DataTransfer.transfer1(
							BpmnInstanceNodeRecipient.class, _$sqlje_rs_gen17);
					if (binc.user_id == null) {
					}
				} catch (NoDataFoundException e) {
				}
			}
		}
		return false;
	}

	public boolean autoPass(Long instance_id, Long usertask_id, Long user_id)
			throws Exception {
		String _$sqlje_sql_gen61 = "\n\t\t\tselect count(1) from bpmn_instance_node_hierarchy h\n\t\t\twhere h.instance_id=?\n\t\t\tand h.usertask_id = ?\n\t\t\tand coalesce(h.disabled_flag,'N')='N'\n\t\t";
		PreparedStatement _$sqlje_ps_gen60 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen61);
		_$sqlje_ps_gen60.setLong(1, instance_id);
		_$sqlje_ps_gen60.setLong(2, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen60.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen60.getUpdateCount();
		ResultSet _$sqlje_rs_gen18 = _$sqlje_ps_gen60.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen18);
		getSqlCallStack().push(_$sqlje_ps_gen60);
		Long count_hirc = DataTransfer.transfer1(Long.class, _$sqlje_rs_gen18);
		String _$sqlje_sql_gen63 = "\n\t\t\tselect can_no_approver from bpmn_usertask_node\n\t\t\twhere usertask_id = ?\n\t\t";
		PreparedStatement _$sqlje_ps_gen62 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen63);
		_$sqlje_ps_gen62.setLong(1, usertask_id);
		$sql.clear();
		_$sqlje_ps_gen62.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen62.getUpdateCount();
		ResultSet _$sqlje_rs_gen19 = _$sqlje_ps_gen62.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen19);
		getSqlCallStack().push(_$sqlje_ps_gen62);
		Long can_no_approver = DataTransfer.transfer1(Long.class,
				_$sqlje_rs_gen19);
		if (can_no_approver == null)
			can_no_approver = 0L;
		if (count_hirc == 0 && can_no_approver == 1) {
			return true;
		}
		return false;
	}

	public void commision(CompositeMap param) throws Exception {
		commision(param.getLong("record_id"), param.getLong("user_id"),
				param.getString("delivercomment"));
	}

	/**
	 * 工作流转交
	 */
	public void commision(Long rcpt_id, Long user_id, String comment)
			throws Exception {
		String _$sqlje_sql_gen65 = "select r.instance_id,\n\t\t\t\t\t\t\t       r.usertask_id,\n\t\t\t\t\t\t\t       r.seq_number,\n\t\t\t\t\t\t\t       r.date_limit,\n\t\t\t\t\t\t\t       r.user_id,\n\t\t\t\t\t\t\t       r.hierarchy_record_id,\n\t\t\t\t\t\t\t       h.rule_record_id,\n\t\t\t\t\t\t\t       h.rule_detail_id\n\t\t\t\t\t\t\t  from bpmn_instance_node_recipient r\n\t\t\t\t\t\t\t  left outer join bpmn_instance_node_hierarchy h\n\t\t\t\t\t\t\t    on r.hierarchy_record_id = h.hierarchy_record_id\n\t\t\t\t\t\t\t where r.record_id = ? ";
		PreparedStatement _$sqlje_ps_gen64 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen65);
		_$sqlje_ps_gen64.setLong(1, rcpt_id);
		$sql.clear();
		_$sqlje_ps_gen64.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen64.getUpdateCount();
		ResultSet _$sqlje_rs_gen20 = _$sqlje_ps_gen64.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen20);
		getSqlCallStack().push(_$sqlje_ps_gen64);
		CompositeMap c_rcpt = DataTransfer.transfer1(CompositeMap.class,
				_$sqlje_rs_gen20);
		Long instance_id = c_rcpt.getLong("instance_id");
		Long usertask_id = c_rcpt.getLong("usertask_id");
		Long seq_number = c_rcpt.getLong("seq_number");
		Long user_id_from = c_rcpt.getLong("user_id");
		Long rule_record_id = c_rcpt.getLong("rule_record_id");
		Long rule_detail_id = c_rcpt.getLong("rule_detail_id");
		DefaultRecipientRules def_rcpt_rule_proc = getInstanceManager()
				.createInstance(DefaultRecipientRules.class, this);
		Long hirc_id = def_rcpt_rule_proc.insertBpmInstanceNodeHirc(
				instance_id, usertask_id, seq_number, user_id, "Y", "N", "由"
						+ user_id_from + "转交", rule_record_id, rule_detail_id,
				user_id_from, null);
		BpmnInstanceNodeRecipient binr = new BpmnInstanceNodeRecipient();
		binr.instance_id = instance_id;
		binr.usertask_id = usertask_id;
		binr.seq_number = seq_number;
		binr.user_id = user_id;
		binr.commision_by = user_id_from;
		binr.commision_desc = comment;
		binr.hierarchy_record_id = hirc_id;
		binr.attachment_id = c_rcpt.getLong("attachment_id");
		binr.date_limit = (java.sql.Date) c_rcpt.get("date_limit");
		$sql.insert(binr);
		String _$sqlje_sql_gen67 = "select  r.usertask_id,\n                                    i.instance_id,\n                                    r.seq_number,\n                                    r.user_id\n                               from bpmn_instance_node_recipient r,\n                                    bpmn_process_instance       i\n                              where r.record_id = ?\n                                and r.instance_id = i.instance_id";
		PreparedStatement _$sqlje_ps_gen66 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen67);
		_$sqlje_ps_gen66.setLong(1, rcpt_id);
		$sql.clear();
		_$sqlje_ps_gen66.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen66.getUpdateCount();
		ResultSet _$sqlje_rs_gen21 = _$sqlje_ps_gen66.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen21);
		getSqlCallStack().push(_$sqlje_ps_gen66);
		CompositeMap c_approve_record = DataTransfer.transfer1(
				CompositeMap.class, _$sqlje_rs_gen21);
		BpmnApproveRecord bar = new BpmnApproveRecord();
		bar.usertask_id = usertask_id;
		bar.instance_id = instance_id;
		bar.seq_number = seq_number;
		bar.action_token = "COMMISION";
		bar.comment_text = comment;
		bar.rcpt_record_id = rcpt_id;
		$sql.insert(bar);
		Long hir_rec_id = c_rcpt.getLong("hierarchy_record_id");
		String _$sqlje_sql_gen69 = "update bpmn_instance_node_hierarchy h \n\t\t\tset h.disabled_flag='Y',\n\t\t\t\th.note='COMMISION'\n\t\t\twhere h.hierarchy_record_id=? ";
		PreparedStatement _$sqlje_ps_gen68 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen69);
		_$sqlje_ps_gen68.setLong(1, hir_rec_id);
		$sql.clear();
		_$sqlje_ps_gen68.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen68.getUpdateCount();
		ResultSet _$sqlje_rs_gen22 = _$sqlje_ps_gen68.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen22);
		getSqlCallStack().push(_$sqlje_ps_gen68);
		String _$sqlje_sql_gen71 = "delete from bpmn_instance_node_recipient r where r.record_id=?";
		PreparedStatement _$sqlje_ps_gen70 = getSqlCallStack()
				.getCurrentConnection().prepareStatement(_$sqlje_sql_gen71);
		_$sqlje_ps_gen70.setLong(1, rcpt_id);
		$sql.clear();
		_$sqlje_ps_gen70.execute();
		$sql.UPDATECOUNT = _$sqlje_ps_gen70.getUpdateCount();
		ResultSet _$sqlje_rs_gen23 = _$sqlje_ps_gen70.getResultSet();
		getSqlCallStack().push(_$sqlje_rs_gen23);
		getSqlCallStack().push(_$sqlje_ps_gen70);
		ProcessLogProc.ProcessLog bpl = new ProcessLogProc.ProcessLog();
		bpl.instance_id = instance_id;
		bpl.user_id = user_id_from;
		bpl.event_type = "COMMISION";
		bpl.log_content = "工作流转交:rcpt_id=" + rcpt_id;
		$sql.insert(bpl);
	}

	public static boolean eq(Object o1, Object o2) {
		if (o1 == null)
			return o2 == null;
		return o1.equals(o2);
	}

	protected aurora.sqlje.core.IInstanceManager _$sqlje_instanceManager = null;
	protected aurora.sqlje.core.ISqlCallStack _$sqlje_sqlCallStack = null;
	protected SqlFlag $sql = new SqlFlag(this);

	public aurora.sqlje.core.IInstanceManager getInstanceManager() {
		return _$sqlje_instanceManager;
	}

	public void _$setInstanceManager(aurora.sqlje.core.IInstanceManager args0) {
		_$sqlje_instanceManager = args0;
	}

	public void _$setSqlCallStack(aurora.sqlje.core.ISqlCallStack args0) {
		_$sqlje_sqlCallStack = args0;
	}

	public aurora.sqlje.core.ISqlCallStack getSqlCallStack() {
		return _$sqlje_sqlCallStack;
	}
}
