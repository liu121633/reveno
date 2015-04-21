/** 
 *  Copyright (c) 2015 The original author or authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.reveno.atp.core.restore;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.reveno.atp.api.transaction.EventBus;
import org.reveno.atp.commons.LongRange;
import org.reveno.atp.core.api.EventsCommitInfo;
import org.reveno.atp.core.api.RestoreableEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestorerEventBus implements RestoreableEventBus {

	@Override
	public void publishEvent(Object event) {
		Iterator<LongRange> i = unpublishedEvents.iterator();
		while (i.hasNext()) {
			LongRange range = i.next();
			if (!range.higher(currentTransactionId)) {
				if (!range.contains(currentTransactionId)) {
					underlyingEventBus.publishEvent(event);
				}
				break;
			}
			i.remove();
		}
	}
	
	@Override
	public RestoreableEventBus currentTransactionId(long transactionId) {
		this.currentTransactionId = transactionId;
		return this;
	}
	
	@Override
	public RestoreableEventBus underlyingEventBus(EventBus eventBus) {
		this.underlyingEventBus = eventBus;
		return this;
	}
	
	public void processNextEvent(EventsCommitInfo event) {
		if (lastTransactionId == -1L) {
			lastTransactionId = event.getTransactionId();
			return;
		}
		
		if (event.getTransactionId() <= lastTransactionId) 
			throw new RuntimeException("Next TxID can't be less or equal than last TxID!");
		
		if (event.getTransactionId() - lastTransactionId > 1) {
			log.info("Missing transaction events from {} to {}", lastTransactionId + 1, event.getTransactionId() - 1);
			unpublishedEvents.add(new LongRange(lastTransactionId + 1, event.getTransactionId() - 1));
		}
	}
	
	public Set<LongRange> getUnpublishedEvents() {
		return unpublishedEvents;
	}
	
	public void clear() {
		unpublishedEvents.clear();
	}
	
	
	protected EventBus underlyingEventBus;
	protected long currentTransactionId = -1L;
	protected long lastTransactionId = -1L;
	protected TreeSet<LongRange> unpublishedEvents = new TreeSet<LongRange>();
	protected static final Logger log = LoggerFactory.getLogger(RestorerEventBus.class);
}