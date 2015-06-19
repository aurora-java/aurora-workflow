package aurora.bpm.command.beans;

import aurora.sqlje.core.annotation.PK;
import aurora.sqlje.core.annotation.Table;

@Table(name = "bpmn_process_instance", stdwho = true)
public class BpmnProcessInstance {
	@PK
	public Long instance_id;
	public String status;
	public Long parent_id;
	public String process_code;
	public String process_version;
	public String description;
	public Long instance_param;
	/** 创建日期 */
	public java.sql.Date creation_date;
	/** 创建用户ID */
	public Long created_by;
	/** 最后更新日期 */
	public java.sql.Date last_update_date;
	/** 最后更新用户ID */
	public Long last_updated_by;
}
