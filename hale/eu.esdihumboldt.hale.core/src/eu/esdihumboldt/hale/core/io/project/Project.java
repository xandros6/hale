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

package eu.esdihumboldt.hale.core.io.project;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;
import org.osgi.framework.Version;
import org.xml.sax.InputSource;

/**
 * Represents a project.
 * @author Simon Templer
 */
public class Project {
	
	/**
	 * Load a project from an input stream.
	 * @param in the input stream
	 * @return the project
	 * 
	 * @throws MappingException if the mapping could not be loaded
	 * @throws MarshalException if the project could not be read
	 * @throws ValidationException if the input stream did not provide valid XML
	 */
	public static Project load(InputStream in) throws MappingException, MarshalException, ValidationException {
		Mapping mapping = new Mapping(Project.class.getClassLoader());
		mapping.loadMapping(new InputSource(
				Project.class.getResourceAsStream("Project.xml")));
		        
		XMLContext context = new XMLContext();
		context.addMapping(mapping);

		Unmarshaller unmarshaller = context.createUnmarshaller();
		try {
			return (Project) unmarshaller.unmarshal(new InputSource(in));
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}
	
	/**
	 * Save a project to an output stream.
	 * @param project the project to save
	 * @param out the output stream
	 * @throws MappingException if the mapping could not be loaded
	 * @throws ValidationException if the mapping is no valid XML 
	 * @throws MarshalException if the project could not be marshaled 
	 * @throws IOException if the output could not be written 
	 */
	public static void save(Project project, OutputStream out) throws MappingException, MarshalException, ValidationException, IOException {
		Mapping mapping = new Mapping(Project.class.getClassLoader());
		mapping.loadMapping(new InputSource(
				Project.class.getResourceAsStream("Project.xml")));
		        
		XMLContext context = new XMLContext();
		context.addMapping(mapping);
		Marshaller marshaller = context.createMarshaller();
		Writer writer = new BufferedWriter(new OutputStreamWriter(out));
		try {
			marshaller.setWriter(writer);
			marshaller.marshal(project);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}
	
	private String name;
	
	private String author;
	
	private Version haleVersion;
	
	private Date created;
	
	private Date modified;
	
	private final List<IOConfiguration> configurations = new ArrayList<IOConfiguration>();

	//TODO other stuff
	
	/**
	 * @return the configurations
	 */
	public List<IOConfiguration> getConfigurations() {
		return configurations;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the haleVersion
	 */
	public Version getHaleVersion() {
		return haleVersion;
	}

	/**
	 * @param haleVersion the haleVersion to set
	 */
	public void setHaleVersion(Version haleVersion) {
		this.haleVersion = haleVersion;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}
	
}
