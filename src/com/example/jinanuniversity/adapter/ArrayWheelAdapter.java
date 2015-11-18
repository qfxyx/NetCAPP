package com.example.jinanuniversity.adapter;

public class ArrayWheelAdapter<T> implements WheelAdapter {
	
	public static final int DEFAULT_LENGTH = -1;
	private T items[];
	private int length;

	public ArrayWheelAdapter(T items[], int length) {
		this.items = items;
		this.length = length;
	}

	public ArrayWheelAdapter(T items[]) {
		this(items, DEFAULT_LENGTH);
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < items.length) {
			return items[index].toString();
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return items.length;
	}

	@Override
	public int getMaximumLength() {
		return length;
	}
}
