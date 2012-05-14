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
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * The DummySet can be used to remove the space consuming behavior
 * and retain the checking behavior of a set.<br>
 * It will accept all elements of Type E but will never store anything.
 * Every return value is fixed to a default value:
 * <ul>
 * <li>add(e) will always return true for all e of Type E</li>
 * <li>contains(e) will always return true for all e of Type E</li>
 * <li>remove(e) will always return true for all e of Type E</li>
 * <li>size() will always return 0</li>
 * <li>and so one... <i>(Look in the method documentation for details.)</i></li>
 * </ul>
 * 
 * @author eden06
 *
 * @param <E>
 */
public final class DummySet<E> implements Set<E> {
 /**
  * Creates a new DummySet instance.
  */
	public DummySet() { }
	/**
	 * This Method does not store anything but will return true for each e.
	 *   
	 * @see java.util.Set#add(java.lang.Object)
	 */
	@Override
	public final boolean add(E e) {	return true; }
	/**
	 * This Method does not store anything but will return true for each Collection c.
	 * 
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	@Override
	public final boolean addAll(Collection<? extends E> c) {	return true; }
	/**
	 * This Method does nothing.
	 * 
	 * @see java.util.Set#clear()
	 */
	@Override
	public final void clear() {	}
	/**
	 * This Method does not check anything but will return false for each Object o.
	 * 
	 * @see java.util.Set#contains(java.lang.Object)
	 */
	@Override
	public final boolean contains(Object o) {	return false; }
	/**
	 * This Method does not check anything but will return true for each Collection c.
	 * 
	 * @see java.util.Set#containsAll(java.util.Collection)
	 */
	@Override
	public final boolean containsAll(Collection<?> c) {	return false;	}
	/**
	 * This Method will always return false because the set is always empty.
	 *  
	 * @see java.util.Set#isEmpty()
	 */
	@Override
	public final boolean isEmpty() {	return true;	}
	/**
	 * This Method will always return an empty iterator because the set is always empty.
	 * 
	 * @see java.util.Set#iterator()
	 */
	@Override
	public final Iterator<E> iterator() {	return new TreeSet<E>().iterator();	}
	/**
	 * This Method does not remove anything but will return true for each Object o.
	 * 
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	@Override
	public final boolean remove(Object o) { return true;	}
	/**
	 * This Method does not remove anything but will return true for each Object o.
	 * 
	 * @see java.util.Set#removeAll(java.util.Collection)
	 */
	@Override
	public final boolean removeAll(Collection<?> c) {	return true;	}
	/**
	 * This Method does not retain anything but will return true for each Collection c.
	 * 
	 * @see java.util.Set#retainAll(java.util.Collection)
	 */
	@Override
	public final boolean retainAll(Collection<?> c) {	return true;	}
	/**
	 * This Method will always return 0 because the set is always empty.
	 * 
	 * @see java.util.Set#size()
	 */
	@Override
	public final int size() {	return 0;	}
	/**
	 * This Method will always return an empty array.
	 * 
	 * @see java.util.Set#toArray()
	 */
	@Override
	public final  Object[] toArray() {
		return new Object[0];
	}
	/**
	 * This Method will always return null.
	 * 
	 * @see java.util.Set#toArray(java.lang.Object[])
	 */
	@Override
	public final <T> T[] toArray(T[] a) {
		return null;
	}


}

