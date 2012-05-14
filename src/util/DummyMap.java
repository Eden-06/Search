/*******************************************************************************

Copyright (c) 2007, Thomas "Eden_06" Kühn
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this 
  list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or 
  other materials provided with the distribution.
* Neither the name of the Thomas "Eden_06" Kühn nor the names of its 
  contributors may be used to endorse or promote products derived from this 
  software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*******************************************************************************/

package util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * The DummyMap can be used to remove the space consuming behavior
 * and retain the checking behavior of a map.<br>
 * It will accept all mappings from Type K to Type V but will never store anything.
 * Every return value is fixed to a default value:
 * <ul>
 * <li>put(k,v) will always return null for all k of Type k</li>
 * <li>containsKey(k) will always return false for all k of Type K</li>
 * <li>remove(k) will always return null for all k of Type K</li>
 * <li>size() will always return 0</li>
 * <li>and so one... <i>(Look in the method documentation for details.)</i></li>
 * </ul>
 * 
 * @author eden06
 * 
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class DummyMap<K, V> implements Map<K, V> {

	/**
	 * Creates a new instance of DummyMap
	 */
	public DummyMap() {	}

	/**
	 * This method does nothing.
	 * 
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() { }

	/**
	 * This Method does not check anything but will return false for each Object key.
	 * 
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public final boolean containsKey(Object key) {	return false; }

	/**
	 * This Method does not check anything but will return false for each Object value.
	 * 
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public final boolean containsValue(Object value) {	return false; }

	/**
	 * This method will return an empty set (in fact an instance of DummySet).
	 * 
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public final Set<java.util.Map.Entry<K, V>> entrySet() {	
		return new DummySet<java.util.Map.Entry<K, V>>(); 
	}

	/**
	 * This method will return null for every given key.
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public final V get(Object key) {	return null; }

	/**
	 * This method will always return true, because this map will always be empty.
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public final boolean isEmpty() {	return true;	}

	/**
	 * This method will return an empty set (in fact an instance of DummySet).
	 * 
	 * @see java.util.Map#keySet()
	 */
	@Override
	public final Set<K> keySet() {	return new DummySet<K>();	}

	/**
	 * This Method does not store anything but will return null for each key, value pair.
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public final V put(K key, V value) {	return null;	}

	/**
	 * This Method does nothing.
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public final void putAll(Map<? extends K, ? extends V> m) { }

	/**
	 * This Method does not remove anything but will return true for each Object key.
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public final V remove(Object key) {	return null;	}

	/**
	 * This Method will always return 0 because the map is always empty.
	 * 
	 * @see java.util.Map#size()
	 */
	@Override
	public final int size() {	return 0;	}

	/**
	 * This method will return an empty set (an instance of DummySet).
	 * 
	 * @see java.util.Map#values()
	 */
	@Override
	public final Collection<V> values() {	return new DummySet<V>();	}

}
