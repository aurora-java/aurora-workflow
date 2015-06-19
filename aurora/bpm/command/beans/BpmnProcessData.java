package aurora.bpm.command.beans;

import aurora.sqlje.core.annotation.*;

@Table(name = "BPMN_PROCESS_DATA", stdwho = true)
public class BpmnProcessData {
	@PK
	public Long data_id;
	public Long instance_id;
	public String data_object;
	/**创建用户ID*/
	public Long created_by;
	/**创建日期*/
	public java.sql.Date creation_date;
	/**最后更新日期*/
	public java.sql.Date last_update_date;
	/**最后更新用户ID*/
	public Long last_updated_by;
}
