/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.api.common.operators.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.functions.util.FunctionUtils;
import org.apache.flink.api.common.operators.SingleInputOperator;
import org.apache.flink.api.common.operators.UnaryOperatorInformation;
import org.apache.flink.api.common.operators.util.UserCodeClassWrapper;
import org.apache.flink.api.common.operators.util.UserCodeObjectWrapper;
import org.apache.flink.api.common.operators.util.UserCodeWrapper;
import org.apache.flink.api.common.typeutils.TypeSerializer;

/**
 *
 * @param <IN> The input type.
 * @param <OUT> The result type.
 * @param <FT> The type of the user-defined function.
 */
public class MapOperatorBase<IN, OUT, FT extends MapFunction<IN, OUT>> extends SingleInputOperator<IN, OUT, FT> {
	
	public MapOperatorBase(UserCodeWrapper<FT> udf, UnaryOperatorInformation<IN, OUT> operatorInfo, String name) {
		super(udf, operatorInfo, name);
	}
	
	public MapOperatorBase(FT udf, UnaryOperatorInformation<IN, OUT> operatorInfo, String name) {
		super(new UserCodeObjectWrapper<FT>(udf), operatorInfo, name);
	}
	
	public MapOperatorBase(Class<? extends FT> udf, UnaryOperatorInformation<IN, OUT> operatorInfo, String name) {
		super(new UserCodeClassWrapper<FT>(udf), operatorInfo, name);
	}
	
	// --------------------------------------------------------------------------------------------
	
	@Override
	protected List<OUT> executeOnCollections(List<IN> inputData, RuntimeContext ctx, boolean mutableObjectSafeMode) throws Exception {
		MapFunction<IN, OUT> function = this.userFunction.getUserCodeObject();
		
		FunctionUtils.setFunctionRuntimeContext(function, ctx);
		FunctionUtils.openFunction(function, this.parameters);
		
		ArrayList<OUT> result = new ArrayList<OUT>(inputData.size());
		
		if (mutableObjectSafeMode) {
			TypeSerializer<IN> inSerializer = getOperatorInfo().getInputType().createSerializer();
			TypeSerializer<OUT> outSerializer = getOperatorInfo().getOutputType().createSerializer();
			
			for (IN element : inputData) {
				IN inCopy = inSerializer.copy(element);
				OUT out = function.map(inCopy);
				result.add(outSerializer.copy(out));
			}
		} else {
			for (IN element : inputData) {
				result.add(function.map(element));
			}
		}
		
		FunctionUtils.closeFunction(function);
		
		return result;
	}
}
