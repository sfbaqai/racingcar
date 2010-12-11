/* Generic definitions */


#define PACKAGE it.unimi.dsi.fastutil.s
#define VALUE_PACKAGE it.unimi.dsi.fastutil.objects
/* Assertions (useful to generate conditional code) */
#unassert valueclass
#assert valueclass(Object)
#unassert values
 #assert values(reference)
/* Current type and class (and size, if applicable) */
#define KEY_TYPE 
#define VALUE_TYPE Object
#define KEY_CLASS 
#define VALUE_CLASS Object
#if #keyclass(Object) || #keyclass(Reference)
#define KEY_GENERIC_CLASS K
#define KEY_GENERIC_TYPE K
#define KEY_GENERIC <K>
#define KEY_GENERIC_WILDCARD <?>
#define KEY_EXTENDS_GENERIC <? extends K>
#define KEY_SUPER_GENERIC <? super K>
#define KEY_GENERIC_CAST (K)
#define KEY_GENERIC_ARRAY_CAST (K[])
#else
#define KEY_GENERIC_CLASS KEY_CLASS
#define KEY_GENERIC_TYPE KEY_TYPE
#define KEY_GENERIC
#define KEY_GENERIC_WILDCARD
#define KEY_EXTENDS_GENERIC
#define KEY_SUPER_GENERIC
#define KEY_GENERIC_CAST
#define KEY_GENERIC_ARRAY_CAST
#endif
#if #valueclass(Object) || #valueclass(Reference)
#define VALUE_GENERIC_CLASS V
#define VALUE_GENERIC_TYPE V
#define VALUE_GENERIC <V>
#define VALUE_EXTENDS_GENERIC <? extends V>
#define VALUE_GENERIC_CAST (V)
#define VALUE_GENERIC_ARRAY_CAST (V[])
#else
#define VALUE_GENERIC_CLASS VALUE_CLASS
#define VALUE_GENERIC_TYPE VALUE_TYPE
#define VALUE_GENERIC
#define VALUE_EXTENDS_GENERIC
#define VALUE_GENERIC_CAST
#define VALUE_GENERIC_ARRAY_CAST
#endif
#if #keyclass(Object) || #keyclass(Reference)
#if #valueclass(Object) || #valueclass(Reference)
#define KEY_VALUE_GENERIC <K,V>
#define KEY_VALUE_EXTENDS_GENERIC <? extends K, ? extends V>
#else
#define KEY_VALUE_GENERIC <K>
#define KEY_VALUE_EXTENDS_GENERIC <? extends K>
#endif
#else
#if #valueclass(Object) || #valueclass(Reference)
#define KEY_VALUE_GENERIC <V>
#define KEY_VALUE_EXTENDS_GENERIC <? extends V>
#else
#define KEY_VALUE_GENERIC
#define KEY_VALUE_EXTENDS_GENERIC
#endif
#endif
/* Value methods */
#define KEY_VALUE Value
#define VALUE_VALUE ObjectValue
/* Interfaces (keys) */
#define COLLECTION Collection

#define SET Set

#define SORTED_SET SortedSet

#define STD_SORTED_SET SortedSet

#define FUNCTION 2ObjectFunction
#define MAP 2ObjectMap
#define SORTED_MAP 2ObjectSortedMap
#if #keyclass(Object) || #keyclass(Reference)
#define STD_SORTED_MAP SortedMap

#else
#define STD_SORTED_MAP 2ObjectSortedMap

#endif
#define LIST List

#define STACK Stack

#define PRIORITY_QUEUE PriorityQueue

#define INDIRECT_PRIORITY_QUEUE IndirectPriorityQueue

#define INDIRECT_DOUBLE_PRIORITY_QUEUE IndirectDoublePriorityQueue

#define KEY_ITERATOR Iterator

#define KEY_ITERABLE Iterable

#define KEY_BIDI_ITERATOR BidirectionalIterator

#define KEY_LIST_ITERATOR ListIterator

#define STD_KEY_ITERATOR Iterator

#define KEY_COMPARATOR Comparator

/* Interfaces (values) */
#define VALUE_COLLECTION ObjectCollection

#define VALUE_ARRAY_SET ObjectArraySet

#define VALUE_ITERATOR ObjectIterator

#define VALUE_LIST_ITERATOR ObjectListIterator

/* Abstract implementations (keys) */
#define ABSTRACT_COLLECTION AbstractCollection

#define ABSTRACT_SET AbstractSet

#define ABSTRACT_SORTED_SET AbstractSortedSet
#define ABSTRACT_FUNCTION Abstract2ObjectFunction
#define ABSTRACT_MAP Abstract2ObjectMap
#define ABSTRACT_FUNCTION Abstract2ObjectFunction
#define ABSTRACT_SORTED_MAP Abstract2ObjectSortedMap
#define ABSTRACT_LIST AbstractList

#define SUBLIST SubList

#define ABSTRACT_PRIORITY_QUEUE AbstractPriorityQueue

#define ABSTRACT_STACK AbstractStack

#define KEY_ABSTRACT_ITERATOR AbstractIterator

#define KEY_ABSTRACT_BIDI_ITERATOR AbstractBidirectionalIterator

#define KEY_ABSTRACT_LIST_ITERATOR AbstractListIterator

#if #keyclass(Object)
#define KEY_ABSTRACT_COMPARATOR Comparator

#else
#define KEY_ABSTRACT_COMPARATOR AbstractComparator

#endif
/* Abstract implementations (values) */
#define VALUE_ABSTRACT_COLLECTION AbstractObjectCollection

#define VALUE_ABSTRACT_ITERATOR AbstractObjectIterator

#define VALUE_ABSTRACT_BIDI_ITERATOR AbstractObjectBidirectionalIterator

/* Static containers (keys) */
#define COLLECTIONS Collections

#define SETS Sets

#define SORTED_SETS SortedSets

#define LISTS Lists

#define MAPS 2ObjectMaps
#define FUNCTIONS 2ObjectFunctions
#define SORTED_MAPS 2ObjectSortedMaps
#define PRIORITY_QUEUES PriorityQueues

#define HEAPS Heaps

#define SEMI_INDIRECT_HEAPS SemiIndirectHeaps

#define INDIRECT_HEAPS IndirectHeaps

#define ARRAYS Arrays

#define ITERATORS Iterators

#define COMPARATORS Comparators

/* Static containers (values) */
#define VALUE_COLLECTIONS ObjectCollections

#define VALUE_SETS ObjectSets

#define VALUE_ARRAYS ObjectArrays

/* Implementations */
#define OPEN_HASH_SET OpenHashSet

#define OPEN_HASH_MAP 2ObjectOpenHashMap

#define ARRAY_SET ArraySet

#define ARRAY_MAP 2ObjectArrayMap

#define LINKED_OPEN_HASH_SET LinkedOpenHashSet

#define AVL_TREE_SET AVLTreeSet

#define RB_TREE_SET RBTreeSet

#define AVL_TREE_MAP 2ObjectAVLTreeMap

#define RB_TREE_MAP 2ObjectRBTreeMap

#define ARRAY_LIST ArrayList

#define ARRAY_FRONT_CODED_LIST ArrayFrontCodedList

#define HEAP_PRIORITY_QUEUE HeapPriorityQueue

#define HEAP_SEMI_INDIRECT_PRIORITY_QUEUE HeapSemiIndirectPriorityQueue

#define HEAP_INDIRECT_PRIORITY_QUEUE HeapIndirectPriorityQueue

#define HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUE HeapSesquiIndirectDoublePriorityQueue

#define HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUE HeapIndirectDoublePriorityQueue

#define ARRAY_PRIORITY_QUEUE ArrayPriorityQueue

#define ARRAY_INDIRECT_PRIORITY_QUEUE ArrayIndirectPriorityQueue

#define ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUE ArrayIndirectDoublePriorityQueue

/* Synchronized wrappers */
#define SYNCHRONIZED_COLLECTION SynchronizedCollection

#define SYNCHRONIZED_SET SynchronizedSet

#define SYNCHRONIZED_SORTED_SET SynchronizedSortedSet

#define SYNCHRONIZED_FUNCTION Synchronized2ObjectFunction

#define SYNCHRONIZED_MAP Synchronized2ObjectMap

#define SYNCHRONIZED_LIST SynchronizedList

/* Unmodifiable wrappers */
#define UNMODIFIABLE_COLLECTION UnmodifiableCollection

#define UNMODIFIABLE_SET UnmodifiableSet

#define UNMODIFIABLE_SORTED_SET UnmodifiableSortedSet

#define UNMODIFIABLE_FUNCTION Unmodifiable2ObjectFunction

#define UNMODIFIABLE_MAP Unmodifiable2ObjectMap

#define UNMODIFIABLE_LIST UnmodifiableList

#define UNMODIFIABLE_KEY_ITERATOR UnmodifiableIterator

#define UNMODIFIABLE_KEY_BIDI_ITERATOR UnmodifiableBidirectionalIterator

#define UNMODIFIABLE_KEY_LIST_ITERATOR UnmodifiableListIterator

/* Other wrappers */
#define KEY_READER_WRAPPER ReaderWrapper

#define KEY_DATA_INPUT_WRAPPER DataInputWrapper

/* Methods (keys) */
#define NEXT_KEY next
#define PREV_KEY previous
#define FIRST_KEY firstKey
#define LAST_KEY lastKey
#define GET_KEY get
#define REMOVE_KEY remove
#define READ_KEY read
#define WRITE_KEY write
#define DEQUEUE dequeue
#define SUBLIST_METHOD SubList
#define SINGLETON_METHOD Singleton

#define FIRST first
#define LAST last
#define TOP top
#define PEEK peek
#define POP pop
#define KEY_ITERATOR_METHOD Iterator

#define KEY_LIST_ITERATOR_METHOD ListIterator

#define KEY_EMPTY_ITERATOR_METHOD emptyIterator

#define AS_KEY_ITERATOR asIterator

#define TO_KEY_ARRAY toArray
#define ENTRY_GET_KEY getKey
#define PARSE_KEY parse
#define LOAD_KEYS loads
#define STORE_KEYS stores
/* Methods (values) */
#define NEXT_VALUE next
#define PREV_VALUE previous
#define READ_VALUE readObject
#define WRITE_VALUE writeObject
#define VALUE_ITERATOR_METHOD objectIterator

#define ENTRY_GET_VALUE getValue
/* Methods (keys/values) */
#define ENTRYSET 2ObjectEntrySet
/* Methods that have special names depending on keys (but the special names depend on values) */
#if #keyclass(Object) || #keyclass(Reference)
#define GET_VALUE get
#define REMOVE_VALUE remove
#else
#define GET_VALUE get
#define REMOVE_VALUE remove
#endif
/* Equality */
#if #keyclass(Object)
#ifdef Custom
#define KEY_EQUALS(x,y) ( strategy.equals( (x), (y) ) )
#define KEY_EQUALS_HASH(x,h,y) ( (y) != HashCommon.REMOVED && (h) == strategy.hashCode(y) && strategy.equals((x), (y)) )
#else
#define KEY_EQUALS(x,y) ( (x) == null ? (y) == null : (x).equals(y) )
#define KEY_EQUALS_NOT_NULL(x,y) ( (x).equals(y) )
#define KEY_EQUALS_HASH(x,h,y) ( (y) == null ? (x) == null : (h) == (y).hashCode() && (y).equals(x) )
#endif
#else
#define KEY_EQUALS(x,y) ( (x) == (y) )
#define KEY_EQUALS_NOT_NULL(x,y) ( (x) == (y) )
#define KEY_EQUALS_HASH(x,h,y) ( (x) == (y) )
#endif

#if #valueclass(Object)
#define VALUE_EQUALS(x,y) ( (x) == null ? (y) == null : (x).equals(y) )
#else
#define VALUE_EQUALS(x,y) ( (x) == (y) )
#endif

/* Object/Reference-only definitions (keys) */
#if #keyclass(Object) || #keyclass(Reference)
#define REMOVE remove
#define KEY_OBJ2TYPE(x) (x)
#define KEY_CLASS2TYPE(x) (x)
#define KEY2OBJ(x) (x)
#if #keyclass(Object)
#ifdef Custom
#define KEY2INT(x) ( strategy.hashCode(x) )
#else
#define KEY2INT(x) ( (x) == null ? 0 : (x).hashCode() )
#endif
#else
#define KEY2INT(x) (System.identityHashCode(x))
#endif
#define KEY_CMP(x,y) ( ((Comparable<KEY_GENERIC_CLASS>)(x)).compareTo(y) )
#define KEY_LESS(x,y) ( ((Comparable<KEY_GENERIC_CLASS>)(x)).compareTo(y) < 0 )
#define KEY_LESSEQ(x,y) ( ((Comparable<KEY_GENERIC_CLASS>)(x)).compareTo(y) <= 0 )
#define KEY_NULL (null)
#else
/* Primitive-type-only definitions (keys) */
#define REMOVE rem
#define KEY_CLASS2TYPE(x) ((x).KEY_VALUE())
#define KEY_OBJ2TYPE(x) (KEY_CLASS2TYPE((KEY_CLASS)(x)))
#define KEY2OBJ(x) (KEY_CLASS.valueOf(x))
#if #keyclass(Boolean)
#define KEY_CMP(x,y) ( !(x) && (y) ? -1 : ( (x) == (y) ? 0 : 1 ) )
#define KEY_LESS(x,y) ( !(x) && (y) )
#define KEY_LESSEQ(x,y) ( !(x) || (y) )
#else
#define KEY_CMP(x,y) ( (x) < (y) ? -1 : ( (x) == (y) ? 0 : 1 ) )
#define KEY_LESS(x,y) ( (x) < (y) )
#define KEY_LESSEQ(x,y) ( (x) <= (y) )
#endif
#if #keyclass(Float) || #keyclass(Double) || #keyclass(Long)
#define KEY_NULL (0)
#define KEY2INT(x) it.unimi.dsi.fastutil.HashCommon.2int(x)
#elif #keyclass(Boolean)
#define KEY_NULL (false)
#define KEY2INT(x) (x ? 1231 : 1237)
#else
#if #keyclass(Integer)
#define KEY_NULL (0)
#else
#define KEY_NULL ((KEY_TYPE)0)
#endif
#define KEY2INT(x) (x)
#endif
#endif
/* Object/Reference-only definitions (values) */
#if #valueclass(Object) || #valueclass(Reference)
#define VALUE_OBJ2TYPE(x) (x)
#define VALUE_CLASS2TYPE(x) (x)
#define VALUE2OBJ(x) (x)
#if #valueclass(Object)
#define VALUE2INT(x) ( (x) == null ? 0 : (x).hashCode() )
#else
#define VALUE2INT(x) (System.identityHashCode(x))
#endif
#define VALUE_NULL (null)
#define OBJECT_DEFAULT_RETURN_VALUE (this.defRetValue)
#else
/* Primitive-type-only definitions (values) */
#define VALUE_CLASS2TYPE(x) ((x).VALUE_VALUE())
#define VALUE_OBJ2TYPE(x) (VALUE_CLASS2TYPE((VALUE_CLASS)(x)))
#define VALUE2OBJ(x) (VALUE_CLASS.valueOf(x))
#if #valueclass(Float) || #valueclass(Double) || #valueclass(Long)
#define VALUE_NULL (0)
#define VALUE2INT(x) it.unimi.dsi.fastutil.HashCommon.Object2int(x)
#elif #valueclass(Boolean)
#define VALUE_NULL (false)
#define VALUE2INT(x) (x ? 1231 : 1237)
#else
#if #valueclass(Integer)
#define VALUE_NULL (0)
#else
#define VALUE_NULL ((VALUE_TYPE)0)
#endif
#define VALUE2INT(x) (x)
#endif
#define OBJECT_DEFAULT_RETURN_VALUE (null)
#endif
#include "TextIO.drv"

