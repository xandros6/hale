/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                 01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2011.
 */

package eu.esdihumboldt.hale.common.align.transformation.report.impl;

import java.util.Properties;

import eu.esdihumboldt.hale.common.core.report.Message;
import eu.esdihumboldt.hale.common.core.report.impl.AbstractMessageDefinition;

/**
 * Object definition for {@link TransformationMessageImpl}
 * @author Simon Templer
 */
public class TransformationMessageImplDefinition extends AbstractMessageDefinition<TransformationMessageImpl> {
	
	/**
	 * Key for the message string
	 */
	public static final String KEY_MESSAGE = "message";
	
	/**
	 * Key for the stack trace
	 */
	public static final String KEY_STACK_TRACE = "stack";

	/**
	 * Default constructor
	 */
	public TransformationMessageImplDefinition() {
		super(TransformationMessageImpl.class, "default");
	}

	/**
	 * @see AbstractMessageDefinition#createMessage(Properties)
	 */
	@Override
	protected TransformationMessageImpl createMessage(Properties props) {
//		return new TransformationMessageImpl(
//				props.getProperty(KEY_MESSAGE), 
//				null, 
//				props.getProperty(KEY_STACK_TRACE));
		
		return null; // TODO implement me! TransformationMessageImplDefinition.createMessage()
	}

	/**
	 * @see AbstractMessageDefinition#asProperties(Message)
	 */
	@Override
	protected Properties asProperties(TransformationMessageImpl message) {
		Properties props = new Properties();
		
		props.setProperty(KEY_MESSAGE, message.getMessage());
		props.setProperty(KEY_STACK_TRACE, message.getStackTrace());
		
		return props ;
	}
}