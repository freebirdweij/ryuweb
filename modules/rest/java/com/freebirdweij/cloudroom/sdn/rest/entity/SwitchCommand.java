package com.freebirdweij.cloudroom.sdn.rest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.freebirdweij.cloudroom.common.persistence.DataEntity;

/**
 * 交换机命令模块Entity
 * @author Cloudman
 * @version 2014-08-25
 */
@Entity
@Table(name = "switch_command")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SwitchCommand extends DataEntity<SwitchCommand> {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** 日志ID. */
	private Integer id;

	/** 交换机命令. */
	private String cmdName;

	/** 命令执行结果. */
	private String cmdResult;

	/** 命令类型. */
	private String cmdType;
	
	/** 对应交换机. */
	private String switchId;

	/**
	 * Constructor.
	 */
	public SwitchCommand() {
	}

	/**
	 * Set the 日志ID.
	 * 
	 * @param id
	 *            日志ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Get the 日志ID.
	 * 
	 * @return 日志ID
	 */
	@Id
    @GenericGenerator(name = "idGenerator", strategy = "increment")
    @GeneratedValue(generator = "idGenerator")	
	public Integer getId() {
		return this.id;
	}

	public String getCmdName() {
		return cmdName;
	}

	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}

	public String getCmdResult() {
		return cmdResult;
	}

	public void setCmdResult(String cmdResult) {
		this.cmdResult = cmdResult;
	}

	public String getCmdType() {
		return cmdType;
	}

	public void setCmdType(String cmdType) {
		this.cmdType = cmdType;
	}

	public String getSwitchId() {
		return switchId;
	}

	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SwitchCommand other = (SwitchCommand) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
