package aurora.bpm.command.beans;

import aurora.sqlje.core.annotation.*;

@Table(name = "BPMN_PATH_LOG", stdwho = true)
public class BpmnPathLog {
	@PK
	public Long log_id;
	public Long instance_id;
	public Long path_id;
	@InsertExpression("CURRENT_Date")
	public java.sql.Date log_date;
	public String user_id;
	public String current_node;
	public String prev_node;
	public String event_type;
	public String log_content;
	/**创建用户ID*/
	public Long created_by;
	/**创建日期*/
	public java.sql.Date creation_date;
	/**最后更新日期*/
	public java.sql.Date last_update_date;
	/**最后更新用户ID*/
	public Long last_updated_by;
}
