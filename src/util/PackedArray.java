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

import java.util.Arrays;

/**
 * This is the implementation of a simple packing algorithm,
 * to reduce the size of an integer array with a fixed number of positive elements and 
 * where the highest value is known.
 * This implementation allows further extraction and injection of values,
 * with out unpacking and packing the howl array again.<br>
 * <br>
 * The implementation is based on three simple bit twiddling operations 
 * on integer values: PackedArray.ld, PackedArray.extract and PackedArray.inject.<br>
 * The idea of this algorithm is that if the largest integer is known,
 * we can represent every element by a fixed number of bits 
 * <i>(hopefully smaller then the actual size of the integer)</i>.<br>
 * Assume that the number of bits is three and we have a array of 20
 * elements, we can store the whole array in an array consisting of only two integer values.
 * Each such integer value contains 10 sections (each three bits long).
 * So every element of the original array can be stored in one of the 20 sections.
 * With that we have reduced the size of a 80 Byte array to 8 Byte.<br>
 * <br>
 * Because we have sliced each integer in a fixed size of peaces it is easy,
 * to access each value in constant time, without unpacking the array.<br>
 * <b>Note:</b> This implementation can is only applicable if the following constraints hold:
 * <ul>
 * <li>The array contains only integer values</li>
 * <li>Every element of the array is a positive value</li>
 * <li>You know that the values can not grow larger than a certain value</li>
 * <li>The size of the array never changes</li>
 * <li>You want to have read and write access to the packed array</li>
 * </ul>  
 * 
 * @author eden06
 *
 */
public class PackedArray{
 private int length;
 private int bits;
 private int[] packed;
 
 /**
  * Computes the logarithm to the base 2 for the given integer value.
  * 
  * @param value for the logarithm 
  * @return the ceiling of the logarithm of the value to the base 2
  */
 public static int ld(int value){
 	if (value==0) return 0;
 	return Integer.SIZE-Integer.numberOfLeadingZeros(value-1);
 }
 
 /**
  * This method extracts a section of an integer,
  * by slicing the integer into equal parts of the given size
  * and extracting the part with the given index.<br>
  * For example would <code>PackedArray.extract(0x789ABCDE, 3, 4)</code> return <code>0x0000009</code> as a result.
  * 
  * 
  * @param source from which a part has to be extracted
  * @param index of the section holding the result <i>(starting from zero)</i>
  * @param bits the size of each section
  * @return the extracted value from the source
  */
 public static int extract(int source, int index, int bits){
  /*
    value=0x789ABCDE index=3 bits=4
    32-(2+1)*4                  = 20
    0x789ABCDE >> 20        = 0xFFFFF789A
    0xFFFFF789 & 0x0000000F = 9
  */
  int shift = Integer.SIZE - (index+1) * bits;
  return ( source>>shift ) & ( (1<<bits)-1 );
 }

 /**
  * This method injects the given value into a section of the given integer. 
  * In detail the integer is sliced into equal parts of the given size.
  * Afterwards the given value is injected into the section with the given index.<br>
  * For example would <code>PackedArray.extract(0x789ABCDE, 3, 4, 0x00000005)</code> return <code>0x785ABCDE</code> as a result.
  * 
  * @param source where the value should be injected
  * @param index of the section where the value should be placed <i>(starting from zero)</i>
  * @param bits the size of each section
  * @param value to inject into the source
  * 
  * @return the modified source (where the value has been injected).
  * 
  * @throws IllegalArgumentException if the value fits not in a section
  */
 public static int inject(int source, int index, int bits, int value){
  /*
   old=0x789ABCDE index=3 bits=4 value=5
   32-(2+1)*4                  = 20
   0x0000000F << 20        = 0x00F00000
   ~ 0x00F00000            = 0xFF0FFFFF
   0xFF0FFFFF & 0x789ABCDE = 0x780ABCDE
   5 << 20                 = 0x00500000
   0x780ABCDE | 0x00500000 = 0x785ABCDE
  */
  if (ld(value)>bits)
  	throw new IllegalArgumentException(String.format("value %d does not fit in %d bits",value,bits));
  int shift = Integer.SIZE - (index+1) * bits;
  return (source & ( ~(((1<<bits)-1)<<shift) ) ) | (value<<shift);
 }

 /**
  * Creates a new copy of the given packed array.<br>
  * <b>Note:</b> Use this constructor to create copies of a PackedArray instance.
  * 
  * @param packedArray to copy from
  */
 public PackedArray(PackedArray packedArray){
 	if (packedArray==null) throw new IllegalArgumentException("packed must not be null!");
 	this.length=packedArray.length;
  this.bits=packedArray.bits;
  //initialize array
  int l=Integer.SIZE/bits;
  int n= (length%l==0) ? length/l : length/l+1;
  this.packed=Arrays.copyOf(packedArray.packed,n);
 }
 
 /**
  * Creates a new PackedArray by packing the given integer
  * array to a from where at least values of the given high can be stored.
  * 
  * @param array to be represented by a packed array
  * @param high the highest value of possible elements in of this array
  */
 public PackedArray(int[] array, int high){
 	this.length=array.length;
 	this.bits=ld(high);
 	int l=Integer.SIZE/bits;
 	int n=(length%l==0)?length/l:length/l+1;
 	this.packed=new int[n];
 	int j=-1;
 	for(int i=0;i<n*l;i++){
 		if (j<i/l){
 			j++;
 			packed[j]=0;
 		}else{
 			packed[j]=packed[j] << bits;
 		}
 		if (i<length)	packed[j]=packed[j] | array[i];
 	}  
 }

 
 /**
  * Returns the element at the specified position in the packed array. 
  * 
  * @param index of the element to return
  * @return the value of the indexed component 
  * @throws ArrayIndexOutOfBoundsException if the index is out of range (index < 0 || index >= length())
  */
 public int get(int index){
  if (index<0 || index>length-1) throw new ArrayIndexOutOfBoundsException();
  int l=Integer.SIZE/bits;
  return extract(packed[index/l],index%l,bits);
 }

 /**
  * Replaces the element at the specified position in this list with the specified element. 
  * 
  * @param index index of the element to replace
  * @param element to be stored at the specified position 
  * @throws ArrayIndexOutOfBoundsException if the index is smaller zero or greater then index of the last element
  */
 public void set(int index,int element){
  if (index<0 || index>length-1) throw new ArrayIndexOutOfBoundsException();
  int l=Integer.SIZE/bits;
  packed[index/l]=inject(packed[index/l],index%l,bits,element);
 }

 /**
  * Unpacks the internal representation to
  * a full integer array where every value is stored in every element.
  * 
  * @return unpacked integer array
  */
 public int[] unpack(){
 	int[] result=new int[length];
 	int l=Integer.SIZE/bits;
 	int n=(length%l==0)?length/l:length/l+1;
 	int[] a=Arrays.copyOf(packed,n);
 	int j=-1;
 	int stuff=(1<<bits)-1;
 	for (int i=0;i<n*l;i++){
 		if (j<i/l){
 			j++;
 		}else{
 			a[j]=a[j]>>bits;
 		}
 		if (((2*j+1)*l-1-i)<length)
 		 result[(2*j+1)*l-1-i]=a[j] & stuff;
 	}
 	return result;
 } 

 /**
  * Returns the number of elements in this array.<br>
  * <b>Note:</b> The actual length of the internal array is different.
  * 
  * @return the length of the array
  */
 public int length(){
  return length;
 }
 
 /**
  * Method to test the implementation of the packed array.
  * 
  * @param args no arguments are processed during execution
  */
	public static void main(String[] args) {
		//application to test this class
		int[] array={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
	 PackedArray pack=new PackedArray(array,16);	
	 for(int i=0;i<pack.length();i++){
	 	assert(array[i]==pack.get(i));
	 	assert(array[i]==pack.unpack()[i]);
	 	System.out.format("%d %d %d\n",array[i],pack.get(i),pack.unpack()[i]);
	 }
	 for(int i=0;i<pack.length();i++){
	 	array[i]=(15-i);
	 	pack.set(i,15-i);
	 }
	 for(int i=0;i<pack.length();i++){
	 	assert(array[i]==pack.get(i));
	 	assert(array[i]==pack.unpack()[i]);
	 	System.out.format("%d %d %d\n",array[i],pack.get(i),pack.unpack()[i]);
	 }	
	}

	/**
	 * Returns the internal representation of the integer array.
	 * 
	 * @return the packed array internaly used
	 */
	public int[] packed() {
		return packed;
	}

}
