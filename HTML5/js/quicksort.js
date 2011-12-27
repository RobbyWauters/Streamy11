/**
 * calls the recursive quicksort function
 * 
 * @param array {array}
 */
function quick_sort(array){
	qsort(array, 0, array.length);
}

/**
 * recursive quicksort function that makes a pivot and subdivides the list
 * 
 * @param array {array}
 * @param begin {int}
 * @param end {int}
 */
function qsort(array, begin, end){
	if(end-1>begin) {
		var pivot = begin + Math.floor(Math.random()*(end-begin));

		pivot = partition(array, begin, end, pivot);

		qsort(array, begin, pivot);
		qsort(array, pivot + 1, end);
	}
}

/**
 * swaps two elements in an array
 * 
 * @param array {array}
 * @param a {object}
 * @param b {object}
 */
function swap(array, a, b){
	var tmp = array[a];
	array[a] = array[b];
	array[b] = tmp;
}

/**
 * partitions an array 
 * 
 * @param array {array}
 * @param begin {int}
 * @param end {int}
 * @param pivot {int}
 * @returns {int}
 */
function partition(array, begin, end, pivot){
	var piv = array[pivot].datestamp;
	
	swap(array,pivot, end - 1);
	var store = begin;
	var ix;
	
	for(ix = begin; ix < end-1; ++ix) {
		if(array[ix].datestamp >= piv) {
			swap(array,store, ix);
			++store;
		}
	}
	swap(array, end - 1, store);

	return store;
}