package aurora.bpm.command.beans;

import aurora.sqlje.core.annotation.PK;
import aurora.sqlje.core.annotation.Table;

@Table(name = "bpmn_process_token", stdwho = true)
public class BpmnProcessToken {
	@PK
	public Long token_id;
	public Long instance_id;
	public Long path_id;
	public String node_id;
	/**创建用户ID*/
	public Long created_by;
	/**创建日期*/
	public java.sql.Date creation_date;
	/**最后更新日期*/
	public java.sql.Date last_update_date;
	/**最后更新用户ID*/
	public Long last_updated_by;
}
