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

package org.apache.flink.test.iterative.nephele;

import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.test.iterative.nephele.customdanglingpagerank.CustomCompensatableDanglingPageRankWithCombiner;
import org.apache.flink.test.util.RecordAPITestBase;

public class DanglingPageRankWithCombinerNepheleITCase extends RecordAPITestBase {
	
	protected String pagesWithRankPath;
	protected String edgesPath;
	protected String resultPath;

	public DanglingPageRankWithCombinerNepheleITCase(){
		setTaskManagerNumSlots(DOP);
	}
	
	@Override
	protected void preSubmit() throws Exception {
		this.pagesWithRankPath = createTempFile("pagesWithRank", DanglingPageRankNepheleITCase.TEST_VERTICES);
		this.edgesPath = createTempFile("edges", DanglingPageRankNepheleITCase.TEST_EDGES);
		this.resultPath = getTempDirPath("result");
	}

	@Override
	protected JobGraph getJobGraph() throws Exception {
		String[] parameters = new String[] {
			new Integer(DOP).toString(),
			pagesWithRankPath,
			edgesPath,
			resultPath,
			"<none>",
			"2",
			"5",
			"3",
			"30",
			"5",
			"1",
			"0",
			"100",
			"0"
		};
		
		return CustomCompensatableDanglingPageRankWithCombiner.getJobGraph(parameters);
	}
}
