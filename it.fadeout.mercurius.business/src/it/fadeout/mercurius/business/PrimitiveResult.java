package it.fadeout.mercurius.business;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Result object for REST API
 * @author p.campanella
 *
 */
@XmlRootElement
public class PrimitiveResult {
	public Integer IntValue;
	public String StringValue;
	public Double DoubleValue;
	public Boolean BoolValue;
}
