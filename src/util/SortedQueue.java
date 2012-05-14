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

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.SortedSet;

/**
 * An unbounded sorted queue able to update its elements.
 * This queue grants the correct order also when the elements change their ordering.<br>
 * <br> 
 * The elements of the sorted queue are ordered according to their natural ordering, 
 * or by a Comparator provided at queue construction time, 
 * depending on which constructor is used. 
 * A sorted queue does not permit null elements. 
 * A sorted queue relying on natural ordering also
 * does not permit insertion of non-comparable objects (doing so may result in ClassCastException).<br>
 * <br>
 * The head of this queue is the smallest element with respect to the specified ordering.
 * If there are more smallest elements the first inserted will be the head of the queue.  
 * The queue retrieval operations <code>poll, remove</code> and <code>peek</code>
 * search the howl queue to find the current smallest element.<br>
 * <br> 
 * This class and its iterator implement all of the optional methods of the Collection and Iterator interfaces.
 * The Iterator provided in method iterator() is guaranteed to traverse the elements
 * of the sorted queue in the insertion order <b>not the imposed natural ordering</b>.
 * If you need ordered traversal, consider using Arrays.sort(pq.toArray()).<br>
 * <br>
 * <b>Note that this implementation is not synchronized.</b> 
 * Multiple threads should not access a SortedQueue instance concurrently 
 * if any of the threads modifies the queue.<br>
 * <br>
 * Implementation note: this implementation provides
 * constant time for enqueing methods (offer and add) and testing methods (size and isEmpty) 
 * and linear time for the retrieval methods (poll, remove, peek, element and contains); 
 * <br>
 * This class is an extension to the Java Collections Framework. 
 *  
 * @author eden06
 *
 * @param <E> the type of elements held in this collection
 */
public class SortedQueue<E> extends AbstractQueue<E> {
	
 private class Elem{
 	public Elem(E v) {value=v;}
 	public Elem(E v,Elem n,Elem p){
 		value=v; next=n; prev=p;
 	}
 	public E value;
 	public Elem next;
 	public Elem prev;
 }

	//TODO: check if strange behavior of the iterator should be avoided
	private class QIterator implements Iterator<E>{
		private Elem current=null;
		private int isize;
		private boolean removed=true;
		
		public QIterator(){	isize=size;	}
		@Override 
		public boolean hasNext() {	return (current!=anker.prev);	}
		@Override
		public E next() throws ConcurrentModificationException,NoSuchElementException{
			if (isize!=size) throw new ConcurrentModificationException();
			if (current==anker.prev) throw new NoSuchElementException();
			if (current==null)	current=anker; else current=current.next;
			removed=false;
			return current.value;
		}
		@Override
		public void remove() {
//   throw new UnsupportedOperationException("This operation is not supported");
   if (removed) throw new IllegalStateException();
			if (current==anker) anker=current.next;
 		current.next.prev=current.prev;
 		current.prev.next=current.next;
 		size--;
 		isize--;
 		removed=true;
		}
	}
	
	private Comparator<? super E> comparator=null;
	private Elem anker=null;
 private int size=0;
 
	/**
	 * Creates a SortedQueue that orders its elements according to their natural ordering.  
	 */
	public SortedQueue() {}
	
	/**
	 * Creates a SortedQueue with the specified initial capacity that orders its elements according to the specified comparator.
  *  
	 * @param comparator the comparator that will be used to order this sorted queue. 
	 * If null, the natural ordering of the elements will be used
	 */
	public SortedQueue(Comparator<? super E> comparator) {
  this.comparator=comparator;
	}
	
	/**
	 * Creates a SortedQueue containing the elements in the specified collection. 
	 * If the specified collection is an instance of a SortedSet,a PriorityQueue or another SortedSet, 
	 * this sorted queue will be ordered according to the same ordering. 
	 * Otherwise, this priority queue will be ordered according to the natural ordering of its elements.
	 * 
	 * @param c the collection whose elements are to be placed into this priority queue
	 * 
	 * @throws ClassCastException if elements of the specified collection cannot be compared to one another according to the sorted queue's ordering 
  * @throws NullPointerException if the specified collection or any of its elements are null
	 */
	public SortedQueue(Collection<E> c){
		if (c instanceof SortedSet<?>)
			this.comparator = ((SortedSet<E>) c).comparator();
		else if (c instanceof PriorityQueue<?>)
		 this.comparator = ((PriorityQueue<E>) c).comparator();
		else if (c instanceof SortedQueue<?>)
		 this.comparator = ((SortedQueue<E>) c).comparator();
		for (E e : c) offer(e);
	}
	
	/**
	 * Creates a SortedQueue containing the elements in the specified sorted set. 
	 * This sorted queue will be ordered according to the same ordering as the given sorted set. 
	 * 
	 * @param s the sorted set whose elements are to be placed into this sorted queue
	 * 
	 * @throws ClassCastException if elements of c cannot be compared to one another according to sorted set's ordering
	 * @throws NullPointerException if the specified sorted set or any of its elements are null
	 */
	public SortedQueue(SortedSet<E> s){
		this(s.comparator());
		for(E e : s)	offer(e);
	}
	
	/**
	 * Creates a SortedQueue containing the elements in the specified priority queue. 
	 * This sorted queue will be ordered according to the same ordering as the given priority queue. 
	 * 
	 * @param p the priority queue whose elements are to be placed into this sorted queue
	 * 
	 * @throws ClassCastException if elements of c cannot be compared to one another according to p's ordering
	 * @throws NullPointerException if the specified priority queue or any of its elements are null
	 */
	public SortedQueue(PriorityQueue<E> p){
		this(p.comparator());
		for(E e : p) offer(e);
	}
	
	/**
	 * Creates a SortedQueue containing the elements in the specified sorted queue. 
	 * This sorted queue will be ordered according to the same ordering as the given sorted queue. 
	 * 
	 * @param s the sorted queue whose elements are to be placed into this sorted queue
	 * 
	 * @throws ClassCastException if elements of c cannot be compared to one another according to sorted queue's ordering
	 * @throws NullPointerException if the specified sorted queue or any of its elements are null
	 */
	public SortedQueue(SortedQueue<E> s){
		this(s.comparator());
		for(E e : s) offer(e);
	}
 
	/**
	 * Compares two elements of the queue,
	 * by either delegating the comparison to the Comparator object,
	 * or to the natural ordering imposed by the elements.
	 * 
	 * @param a the first object to be compared
	 * @param b the second object to be compared
	 * 
	 * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
	 * 
	 * @throws NullPointerException if one of the arguments is null
	 * @throws ClassCastException if the elements are not comparable 
	 */
 @SuppressWarnings("unchecked")
	private int compare(E a,E b){
 	if ((a==null) || (b==null)) throw new NullPointerException("");
 	if (comparator!=null) return comparator.compare(a,b);
 	return  ((Comparable<E>) a).compareTo(b); // This is the way everybody does this!
 }

 /**
  * Returns an iterator over the elements of this queue.
  * The iterator will return the elements only in the insertion order. 
  * 
  * @see AbstractQueue#iterator()
  */
	@Override
	public Iterator<E> iterator() {
		return new QIterator();
	}
	
 /**
  * @see AbstractQueue#size()
  */
	@Override
	public int size() {
		return size;
	}
	
 /**
  * Inserts the specified element into this priority queue. 
  * @throws ClassCastException if the given element is not comparable
  * @see AbstractQueue#offer(java.lang.Object)
  */
	@Override
	public boolean offer(E value) {
		if (value==null) throw new NullPointerException("SortedQueue prohibit null elements");
		if (compare(value,value)!=0) throw new IllegalArgumentException("Corrupted comparator because value.compareTo(value)!=0");
		if (anker==null){
			anker=new Elem(value);
			anker.prev=anker;
			anker.next=anker;
		}else{
			Elem e=new Elem(value,anker.next,anker);
   anker.next=e;
   e.next.prev=e;
		}
		size++;
		return true;
	}
	
 /**
  * Returns but retains the smallest element in the queue.
  * This method iterates over all elements to find the smallest one.
	 * If there are two smallest elements the first one 
	 * (according to the insertion order) will be selected.<br>
	 * <b>Note:</b> This operation needs O(n) time.
  * 
  * @see AbstractQueue#peek()
  */
	@Override
	public E peek() {
		if (anker==null) return null;
		if (size==1) return anker.value;
		E min=anker.value;
		Elem p=anker.next;
		while (p!=anker){
			if (compare(min,p.value)>0){
				min=p.value;
			}
			p=p.next;
		}	
		return min;
	}

	/**
	 * Returns and removes the smallest element in the queue.
	 * This method iterates over all elements to find the smallest one.
	 * If there are two smallest elements the first one 
	 * (according to the insertion order) will be selected.<br>
	 * <b>Note:</b> This operation needs O(n) time.
	 * 
	 * @see AbstractQueue#poll()
	 */
	@Override
	public E poll() {
		if (anker==null) return null;
		if (size==1){
			E r=anker.value;
			anker=null;
			size=0;
			return r;
		}
		Elem min=anker;
		Elem p=anker.next;
		while (p!=anker){
			if (compare(min.value,p.value)>0){
				min=p;
			}
			p=p.next;
		}
		if (min==anker) anker=min.next;
		min.next.prev=min.prev;
		min.prev.next=min.next;
		size--;
		return min.value;
	}
	
	/**
	 * @see AbstractQueue#clear()
	 */
	public void clear(){
		anker=null;
		size=0;
	}
	
	/**
	 * @see AbstractQueue#isEmpty()
	 */
	public boolean isEmpty(){ 
		return anker==null;
	}
	
	/**
	 * Returns the comparator used to order the elements in this queue,
	 * or null if this queue is sorted according to the natural ordering of its elements.
	 * 
	 * @return the comparator used to order this queue, 
	 * or null if this queue is sorted according to the natural ordering of its elements
	 */
	public Comparator<? super E> comparator(){
		return comparator; 
	}
	
}
