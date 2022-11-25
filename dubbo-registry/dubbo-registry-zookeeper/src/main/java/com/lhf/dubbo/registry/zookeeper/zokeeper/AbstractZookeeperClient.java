package com.lhf.dubbo.registry.zookeeper.zokeeper;

import com.lhf.dubbo.common.utils.ConcurrentHashSet;

import java.util.Set;

public abstract class AbstractZookeeperClient implements ZookeeperClient {
    private final Set<String> persistentExistNodePath = new ConcurrentHashSet<String>(); // 缓存已存在的结点，防止重复创建

//    public

    @Override
    public void create(String path, boolean ephemeral) {
        if (!ephemeral) {
            if (persistentExistNodePath.contains(path)) {
                return;
            }
        }
        if (ephemeral) {
            createEphemeral(path);
        }
    }

    @Override
    public void delete(String path) {

    }

    public void create(String path, String data, boolean ephemeral) {
        if (!ephemeral) {
            if (persistentExistNodePath.contains(path)) {
                return;
            }
        }
        if (ephemeral) {
            createEphemeral(path,data);
        }
    }

    /**
     * 具体创建逻辑交给子类，比如要定制zk结点前缀
     * 并且本类就不必持有zkClient对象
     *
     * @param path
     */
    protected abstract void createEphemeral(String path);

    protected abstract void createPersistent(String path);

    protected abstract void createEphemeral(String path, String data);

    protected abstract void createPersistent(String path, String data);
}
