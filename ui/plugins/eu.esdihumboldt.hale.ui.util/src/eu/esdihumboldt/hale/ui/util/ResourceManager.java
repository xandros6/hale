/*
 * Copyright (c) 2012 Data Harmonisation Panel
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     HUMBOLDT EU Integrated Project #030962
 *     Data Harmonisation Panel <http://www.dhpanel.eu>
 */

package eu.esdihumboldt.hale.ui.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Resource manager that holds resources and offers the possibility to dispose
 * them. Not thread safe as designed for use in the display thread.
 * 
 * @author Simon Templer
 */
public class ResourceManager {

	private Map<Resource<?>, Object> resources = new HashMap<ResourceManager.Resource<?>, Object>();

	/**
	 * Life cycle control for a resource managed by a {@link ResourceManager}.
	 * 
	 * @param <T> the resource type
	 */
	public interface Resource<T> {

		/**
		 * Initialize a resource.
		 * 
		 * @return the resource
		 * @throws Exception if an error occurs creating the resource instance
		 */
		public T initializeResource() throws Exception;

		/**
		 * Dispose a resource.
		 * 
		 * @param resource the resource to dispose
		 */
		public void dispose(T resource);

	}

	/**
	 * Abstract resource type for use in anonymous classes. Is equal to another
	 * resource if they are of the same class.
	 * 
	 * @param <T> the resource type
	 */
	public abstract static class AnonymousClassResource<T> implements Resource<T> {

		@Override
		public int hashCode() {
			return getClass().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			return getClass().equals(obj.getClass());
		}

	}

	/**
	 * Get a resource instance. The same instance is returned for equal resource
	 * definitions.
	 * 
	 * @param resource the resource definition
	 * @return an existing instance of the resource or if none was created yet,
	 *         a new instance
	 * @throws Exception if an error occurs creating a new resource instance
	 */
	@SuppressWarnings("unchecked")
	public <T> T getInstance(Resource<T> resource) throws Exception {
		if (resources.containsKey(resource)) {
			// use the existing resource instance
			return (T) resources.get(resource);
		}
		// create a new one
		T instance = resource.initializeResource();
		resources.put(resource, instance);
		return instance;
	}

	/**
	 * Dispose all resource instances.
	 */
	@SuppressWarnings("unchecked")
	public void dispose() {
		for (Entry<Resource<?>, Object> entry : resources.entrySet()) {
			if (entry.getValue() != null) {
				((Resource<Object>) entry.getKey()).dispose(entry.getValue());
			}
		}
		resources.clear();
	}

}
