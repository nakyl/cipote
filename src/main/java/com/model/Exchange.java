package com.model;

public class Exchange {
	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column exchange.id
	 *
	 * @mbg.generated Tue Jan 09 18:29:23 CET 2018
	 */
	private Integer id;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column exchange.name
	 *
	 * @mbg.generated Tue Jan 09 18:29:23 CET 2018
	 */
	private String name;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column exchange.id
	 *
	 * @return the value of exchange.id
	 *
	 * @mbg.generated Tue Jan 09 18:29:23 CET 2018
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column exchange.id
	 *
	 * @param id
	 *            the value for exchange.id
	 *
	 * @mbg.generated Tue Jan 09 18:29:23 CET 2018
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column exchange.name
	 *
	 * @return the value of exchange.name
	 *
	 * @mbg.generated Tue Jan 09 18:29:23 CET 2018
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column exchange.name
	 *
	 * @param name
	 *            the value for exchange.name
	 *
	 * @mbg.generated Tue Jan 09 18:29:23 CET 2018
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Exchange [id=").append(id).append(", name=").append(name).append("]");
		return builder.toString();
	}
}