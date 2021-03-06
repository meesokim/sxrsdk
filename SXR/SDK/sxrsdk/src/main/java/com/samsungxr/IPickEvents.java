/* Copyright 2016 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsungxr;

/**
 * This interface defines events generated by picking.
 * @see SXRPicker
 * @see SXRPicker.SXRPickedObject
 */
public interface IPickEvents extends IEvents {
    /**
     * Called every frame if any object is picked.
     * {@link SXRPicker#getPicked()} returns the current list of collisions.
     * @param picker SXRPicker which generated the event.
     */
    void onPick(SXRPicker picker);
    
    /**
     * Called every frame if the set of objects picked is empty.
     * @param picker SXRPicker which generated the event.
     */    
    void onNoPick(SXRPicker picker);
    
    /**
     * Called when the pick ray first enters a node.
     * @param sceneObj node picked
     * @param collision information about the collision
     */
    void onEnter(SXRNode sceneObj, SXRPicker.SXRPickedObject collision);
    
    /**
     * Called when the pick ray first exits a node.
     * @param sceneObj node no longer picked.
     */    
    void onExit(SXRNode sceneObj);
    
    /**
     * Called while the pick ray penetrates a node.
     * @param sceneObj node picked
     * @param collision information about the collision
     */    
    void onInside(SXRNode sceneObj, SXRPicker.SXRPickedObject collision);
}
