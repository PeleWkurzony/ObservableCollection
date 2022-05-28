package com.pelewkurzony
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
) : MutableCollection<T> {

    private var elements = MutableList<Any?> (size) { null }
    private var onChange: ((MutableList<Any?>, T, T) -> Unit)? = null
    private var onAdd: ((MutableList<Any?>, Collection<T>) -> Unit)? = null
    private var onRemove: ((MutableList<Any?>, Collection<T?>) -> Unit)? = null
    private var onClear: ((MutableList<Any?>) -> Unit)? = null

    /**
     * Initializes the list with the given [size] and [initialize] function.
     *
     * @param size the size of the list.
     * @param initialize the function to initialize the list.
     */
    init {
        for (i in 0 until size) {
            elements[i] = initialize(i)
        }
    }

    /**
     * Returns the element at the specified [index] in the list.
     * @throws IndexOutOfBoundsException if the specified [index] is out of bounds of this list.
     */
    operator fun get(index: Int): T {
        return elements[index] as T
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * @throws IndexOutOfBoundsException if the specified [index] is out of bounds of this list.
     */
    operator fun set(index: Int, newValue: T) {
        val oldValue = elements[index]
        elements[index] = newValue
        if (oldValue != newValue) {
                this.onChange?.invoke(elements, oldValue as T, newValue)
        }
    }

    /**
     * Sets the onChange listener function.
     * @param onChange the onChange listener function.
     */
    fun addOnChangeListener(onChange: (MutableList<Any?>, T, T) -> Unit) {
        this.onChange = onChange
    }

    /**
     * Removes the onChange listener function.
     */
    fun removeOnChangeListener() {
        this.onChange = null
    }

    /**
     * Sets the onAdd listener function.
     */
    fun addOnAddListener(onAdd: (MutableList<Any?>, Collection<T>) -> Unit) {
        this.onAdd = onAdd
    }

    /**
     * Removes the onAdd listener function.
     */
    fun removeOnAddListener() {
        this.onAdd = null
    }

    /**
     * Sets the onRemove listener function.
     */
    fun addOnRemoveListener(onRemove: (MutableList<Any?>, Collection<T?>) -> Unit) {
        this.onRemove = onRemove
    }

    /**
     * Removes the onRemove listener function.
     */
    fun removeOnRemoveListener() {
        this.onRemove = null
    }

    /**
     * Sets the onClear listener function.
     */
    fun addOnClearListener(onClear: (MutableList<Any?>) -> Unit) {
        this.onClear = onClear
    }

    /**
     * Removes the onClear listener function.
     */
    fun removeOnClearListener() {
        this.onClear = null
    }

    /**
     * Adds the specified element to the end of this list.
     * @param element the element to add.
     * @return true if the list was changed as the result of the operation.
     */
    override fun add(element: T): Boolean {
        // Checks if not null
        onAdd?.invoke(this.elements, listOf(element))
        return this.elements.add(element)
    }

    /**
     * Adds the specified elements to the end of this list.
     * @return true if the list was changed as the result of the operation.
     */
    override fun addAll(elements: Collection<T>): Boolean {
        // Checks if not null
        onAdd?.invoke(this.elements, elements)
        elements.forEach {
            this.add(it)
        }
        return true
    }

    /**
     * Removes all elements from this list.
     */
    override fun clear() {
        this.elements.clear()
    }

    /**
     * Returns true if list has any element.
     */
    override fun isEmpty(): Boolean {
        return this.elements.isEmpty()
    }

    /**
     * Returns true if this list contains the specified elements.
     */
    override fun containsAll(elements: Collection<T>): Boolean {
        return this.elements.containsAll(elements)
    }

    /**
     * Returns true if this list contains the specified element.
     */
    override fun contains(element: T): Boolean {
        return this.elements.contains(element)
    }

    /**
     * Returns a list iterator over the elements in this list (in proper sequence).
     */
    override fun iterator(): MutableIterator<T> {
        return elements.iterator() as MutableIterator<T>
    }

    /**
     * Retains only the elements in this collection that are contained in the specified collection.
     * @return true if any element was removed from the collection, false if the collection was not modified.
     */
    override fun retainAll(elements: Collection<T>): Boolean {
        val removed = this.elements.filter { !elements.contains(it) }
        this.onRemove?.invoke(this.elements, removed as Collection<T?>)
        return this.elements.retainAll(elements)
    }

    /**
     * Removes all of this collection's elements that are also contained in the specified collection.
     * @return true if any of the specified elements was removed from the collection, false if the collection was not modified.
     */
    override fun removeAll(elements: Collection<T>): Boolean {
        this.onRemove?.invoke(this.elements, elements)
        return this.elements.removeAll(elements)
    }

    /**
     * Removes a single instance of the specified element from this collection, if it is present.
     * @return true if the element has been successfully removed; false if it was not present in the collection.
     */
    override fun remove(element: T): Boolean {
        this.onRemove?.invoke(this.elements, listOf(element))
        return this.elements.remove(element)
    }


}