package com.pelewkurzony.observable_collection
// package kotlin.collections
// https://github.com/JetBrains/kotlin/blob/master/libraries/stdlib/src/kotlin/collections/AbstractList.kt

/**
 * A generic ordered collection of elements that supports adding elements, removing elements with additionally provides observer on elements.
 * @param T the type of elements contained in the list. The mutable list is invariant on its element type.
 * @property size the number of elements in the list
 * @property initialize The function [init] is called for each array element sequentially starting from the first one.
 * It should return the value for an array element given its index.
 */
@Suppress("UNCHECKED_CAST")
class ObservableMutableList<T> (
    override val size: Int,
    initialize: (index: Int) -> T
) : MutableList<T> {

    private var _elements = MutableList<Any?> (size) { null }

    private var _onChange: ((MutableList<Any?>, T, T) -> Unit)? = null
    private var _onAdd: ((MutableList<Any?>, Collection<T>) -> Unit)? = null
    private var _onRemove: ((MutableList<Any?>, Collection<T>) -> Unit)? = null
    private var _onClear: ((MutableList<Any?>) -> Unit)? = null

    /**
     * Initializes the list with the given [size] and [initialize] function.
     *
     * @param size the size of the list.
     * @param initialize the function to initialize the list.
     */
    init {
        for (i in 0 until size) {
            _elements[i] = initialize(i)
        }
    }

    /**
     * Returns the element at the specified [index] in the list.
     * @throws IndexOutOfBoundsException if the specified [index] is out of bounds of this list.
     */
    override operator fun get(index: Int): T {
        return _elements[index] as T
    }

    /**
     * Returns first index of element, or -1 if the list does not contain element.
     */
    override fun indexOf(element: T): Int {
        return _elements.indexOf(element)
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * @throws IndexOutOfBoundsException if the specified [index] is out of bounds of this list.
     */
    override operator fun set(index: Int, newValue: T): T {
        val oldValue = _elements[index]
        _elements[index] = newValue
        if (oldValue != newValue) {
                this._onChange?.invoke(_elements, oldValue as T, newValue)
        }
        return oldValue as T
    }

    /**
     * Sets the onChange listener function.
     * @param onChange the onChange listener function.
     */
    fun addOnChangeListener(onChange: (MutableList<Any?>, T, T) -> Unit) {
        this._onChange = onChange
    }

    /**
     * Removes the onChange listener function.
     */
    fun removeOnChangeListener() {
        this._onChange = null
    }

    /**
     * Sets the onAdd listener function.
     */
    fun addOnAddListener(onAdd: (MutableList<Any?>, Collection<T>) -> Unit) {
        this._onAdd = onAdd
    }

    /**
     * Removes the onAdd listener function.
     */
    fun removeOnAddListener() {
        this._onAdd = null
    }

    /**
     * Sets the onRemove listener function.
     */
    fun addOnRemoveListener(onRemove: (MutableList<Any?>, Collection<T?>) -> Unit) {
        this._onRemove = onRemove
    }

    /**
     * Removes the onRemove listener function.
     */
    fun removeOnRemoveListener() {
        this._onRemove = null
    }

    /**
     * Sets the onClear listener function.
     */
    fun addOnClearListener(onClear: (MutableList<Any?>) -> Unit) {
        this._onClear = onClear
    }

    /**
     * Removes the onClear listener function.
     */
    fun removeOnClearListener() {
        this._onClear = null
    }

    /**
     * Adds the specified element to the end of this list.
     * @param element the element to add.
     * @return true if the list was changed as the result of the operation.
     */
    override fun add(element: T): Boolean {
        // Checks if not null
        _onAdd?.invoke(_elements, listOf(element))
        return _elements.add(element)
    }

    /**
     * Inserts an element into the list at the specified index.
     */
    override fun add(index: Int, element: T) {
        _onAdd?.invoke(_elements, listOf(element))
        _elements.add(index, element)
    }


    /**
     * Adds the specified elements to the end of this list.
     * @return true if the list was changed as the result of the operation.
     */
    override fun addAll(elements: Collection<T>): Boolean {
        // Checks if not null
        _onAdd?.invoke(_elements, elements)
        elements.forEach {
            this.add(it)
        }
        return true
    }

    /**
     * Inserts the specified elements at the specified [index] in the list.
     * @return true if the list was changed as the result of the operation.
     */
    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        return _elements.addAll(index, elements)
    }

    /**
     * Removes all elements from this list.
     */
    override fun clear() {
        _elements.clear()
    }

    /**
     * Returns true if list has any element.
     */
    override fun isEmpty(): Boolean {
        return _elements.isEmpty()
    }

    /**
     * Returns true if this list contains the specified elements.
     */
    override fun containsAll(elements: Collection<T>): Boolean {
        return _elements.containsAll(elements)
    }

    /**
     * Returns true if this list contains the specified element.
     */
    override fun contains(element: T): Boolean {
        return _elements.contains(element)
    }

    /**
     * Returns a list iterator over the elements in this list (in proper sequence).
     */
    override fun iterator(): MutableIterator<T> {
        return _elements.iterator() as MutableIterator<T>
    }

    /**
     * Returns a list iterator over the elements in this list (in proper sequence).
     */
    override fun listIterator(): MutableListIterator<T> {
        return _elements.listIterator() as MutableListIterator<T>
    }

    /**
     * Returns a list iterator over the elements in this list (in proper sequence), starting at the specified index.
     */
    override fun listIterator(index: Int): MutableListIterator<T> {
        return _elements.listIterator(index) as MutableListIterator<T>
    }

    /**
     * Removes an element at the specified index from the list.
     * @return the element that has been removed.
     */
    override fun removeAt(index: Int): T {
        return _elements.removeAt(index) as T
    }

    /**
     * Returns a view of the portion of this list between the specified fromIndex (inclusive) and toIndex (exclusive). The returned list is backed by this list, so non-structural changes in the returned list are reflected in this list, and vice-versa.
     * @throws IndexOutOfBoundsException if [fromIndex] is out of range of this list.
     */
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return _elements.subList(fromIndex, toIndex) as MutableList<T>
    }

    /**
     * Returns last index of element, or -1 if the collection does not contain element.
     */
    override fun lastIndexOf(element: T): Int {
        return _elements.lastIndexOf(element)
    }

    /**
     * Retains only the elements in this collection that are contained in the specified collection.
     * @return true if any element was removed from the collection, false if the collection was not modified.
     */
    override fun retainAll(elements: Collection<T>): Boolean {
        val removed = _elements.filter { !elements.contains(it) }
        this._onRemove?.invoke(_elements, removed as Collection<T>)
        return _elements.retainAll(elements)
    }

    /**
     * Removes all of this collection's elements that are also contained in the specified collection.
     * @return true if any of the specified elements was removed from the collection, false if the collection was not modified.
     */
    override fun removeAll(elements: Collection<T>): Boolean {
        this._onRemove?.invoke(_elements, elements)
        return _elements.removeAll(elements)
    }

    /**
     * Removes a single instance of the specified element from this collection, if it is present.
     * @return true if the element has been successfully removed; false if it was not present in the collection.
     */
    override fun remove(element: T): Boolean {
        this._onRemove?.invoke(_elements, listOf(element))
        return _elements.remove(element)
    }


}