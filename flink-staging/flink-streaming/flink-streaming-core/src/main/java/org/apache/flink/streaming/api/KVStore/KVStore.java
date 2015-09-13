/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.streaming.api.KVStore;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.types.KV;

public interface KVStore<K, V> {

	public abstract void put(DataStream<KV<K, V>> stream);

	public abstract int get(DataStream<K> stream);

	public abstract int remove(DataStream<K> stream);

	public abstract <X> int getWithKeySelector(DataStream<X> stream, KeySelector<X, K> keySelector);

	public abstract int multiGet(DataStream<K[]> stream);

	public abstract KVStoreOutput<K, V> getOutputs();

}