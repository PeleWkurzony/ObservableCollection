# Observable Collcetion

## ObservableMutableList

### It's provide listener for MutableList&#60;T&#62;
- onChange `(MutableList<Any?>, T, T) -> Unit)`
- onAdd `(MutableList<Any?>, Collection<T>) -> Unit`
- onRemove `(MutableList<Any?>, Collection<T>) -> Unit`
- onClear `(MutableList<Any?>) -> Unit`

### It's provide function to change listeners in MutableList &#60;T&#62;

1. **OnChange**
- `fun addOnChangeListener(onChange: (MutableList<Any?>, T, T) -> Unit) { ... }`
- `fun removeOnChangeListener() { ... }`

2. **OnAdd**
- `fun addOnAddListener(onAdd: (MutableList<Any?>, Collection<T>) -> Unit) { ... }`
- `fun removeOnAddListener() { ... }`

3. **OnRemove**
- `fun addOnRemoveListener(onRemove: (MutableList<Any?>, Collection<T?>) -> Unit) { ... }`
- `fun removeOnRemoveListener() { ... }`

4. **OnClear**
- `fun addOnClearListener(onClear: (MutableList<Any?>) -> Unit) { ... }`
- `fun removeOnClearListener() { ... }`
