/**
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

package org.apache.zookeeper.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.jute.InputArchive;
import org.apache.jute.OutputArchive;
import org.apache.jute.Record;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.data.StatPersisted;

/**
 * This class contains the data for a node in the data tree.
 * <p>
 * A data node contains a reference to its parent, a byte array as its data, an
 * array of ACLs, a stat object, and a set of its children's paths.
 * 
 */
public class DataNode implements Record {
    /** the parent of this datanode */
    DataNode parent; 
    
    /** the data for this datanode */
    byte data[];     
    
    /** the acl map long for this datanode. 
    the datatree has the map */
    Long acl;       
    
    /** the stat for this node that is persisted 
     * to disk.
     */
    public StatPersisted stat; 

    /** the list of children for this node. note 
     * that the list of children string does not 
     * contain the parent path -- just the last
     * part of the path. This should be 
     * synchronized on except deserializing 
     * (for speed up issues).
     */
    Set<String> children = new HashSet<String>();

    /**
     * default constructor for the datanode
     */
    DataNode() {}

    /**
     * create a DataNode with parent, data, acls and stat
     * @param parent the parent of this DataNode
     * @param data the data to be set
     * @param acl the acls for this node
     * @param stat the stat for this node.
     */
  
    public DataNode(DataNode parent, byte data[], Long acl, StatPersisted stat) {
        this.parent = parent;
        this.data = data;
        this.acl = acl;
        this.stat = stat;
        this.children = new HashSet<String>();
    }

    /**
     * convenience method for creating DataNode
     * fully
     * @param children
     */
    public synchronized void setChildren(HashSet<String> children) {
        this.children = children;
    }
    
    /**
     * convenience methods to get the children
     * @return the children of this datanode
     */
    public synchronized Set<String> getChildren() {
        return this.children;
    }
    
   
    synchronized public void copyStat(Stat to) {
        to.setAversion(stat.getAversion());
        to.setCtime(stat.getCtime());
        to.setCversion(stat.getCversion());
        to.setCzxid(stat.getCzxid());
        to.setMtime(stat.getMtime());
        to.setMzxid(stat.getMzxid());
        to.setPzxid(stat.getPzxid());
        to.setVersion(stat.getVersion());
        to.setEphemeralOwner(stat.getEphemeralOwner());
        to.setDataLength(data == null ? 0 : data.length);
        to.setNumChildren(children.size());
    }

    synchronized public void deserialize(InputArchive archive, String tag)
            throws IOException {
        archive.startRecord("node");
        data = archive.readBuffer("data");
        acl = archive.readLong("acl");
        stat = new StatPersisted();
        stat.deserialize(archive, "statpersisted");
        archive.endRecord("node");
    }

    synchronized public void serialize(OutputArchive archive, String tag)
            throws IOException {
        archive.startRecord(this, "node");
        archive.writeBuffer(data, "data");
        archive.writeLong(acl, "acl");
        stat.serialize(archive, "statpersisted");
        archive.endRecord(this, "node");
    }
}
