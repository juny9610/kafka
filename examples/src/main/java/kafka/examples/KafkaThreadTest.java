/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kafka.examples;

import org.apache.kafka.common.errors.TimeoutException;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class KafkaThreadTest {
    public static void main(String[] args) throws InterruptedException {

        // 0 = sync, 1 = # of substations, 2 = # of sensors, 3 = records count

//        boolean isAsync = args.length == 0 || !args[0].trim().equalsIgnoreCase("sync");
        
		boolean isAsync = true; // false:sync true:async
        //int numSubstations = 1;
        //int numSensors = 64;
        int numRecords = (1 * 1024 * 1024 *100);/// 768; // (total data /record size)
	 	int numThreads = 1000;
        // if(args[1] != null)
        //     numSubstations = Integer.parseInt(args[1]);
        // if(args[2] != null)
        //     numSensors = Integer.parseInt(args[2]);
        // if(args[3] != null)
        //     numRecords = Integer.parseInt(args[3]);

		int recordsPerThread = numRecords;

        String transactionalID = null;
        boolean enableIdempotency = false;
        int transactionTimeoutMs = -1;
        CountDownLatch latch = new CountDownLatch(2);

        Producer producerThread[] = new Producer[numThreads];
        for (int i = 0; i < numThreads; i++) {
            producerThread[i] = new Producer("topic0", isAsync, null, false, recordsPerThread, -1, latch);
        }
/*		for (int i = 0; i < numThreads; i++) {
			if(i%4 == 0) {
				producerThread[i] = new Producer("topic0", isAsync, transactionalID, enableIdempotency,
						                         recordsPerThread, transactionTimeoutMs, latch);
			}

			if(i%4 == 1) {
				producerThread[i] = new Producer("topic1", isAsync, transactionalID, enableIdempotency,
						                         recordsPerThread, transactionTimeoutMs, latch);
			}

			if(i%4 == 2) {
				producerThread[i] = new Producer("topic2", isAsync, transactionalID, enableIdempotency,
						                         recordsPerThread, transactionTimeoutMs, latch);
			}
			else {
				producerThread[i] = new Producer("topic3", isAsync, transactionalID, enableIdempotency,
												 recordsPerThread, transactionTimeoutMs, latch);
			}
		}	*/
		long measureStart = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            producerThread[i].start();
        }
		
		
		/*
        Consumer consumerThread = new Consumer(KafkaProperties.TOPIC, "DemoConsumer", Optional.empty(), false, 10000, latch);
        consumerThread.start();

        if (!latch.await(5, TimeUnit.MINUTES)) {
            throw new TimeoutException("Timeout after 5 minutes waiting for demo producer and consumer to finish");
        }

        consumerThread.shutdown();
		*/

		long measureEnd = System.currentTimeMillis();
		System.out.println("\n==> Total Time is " + (measureEnd - measureStart) + " ms");

        System.out.println("All finished!");
    }
}
