/**
 *
 * 项目名称:[NettyServer]
 * 包:	 [com.sa.service.client]
 * 类名称: [ClientResponecRoomRemove]
 * 类描述: [一句话描述该类的功能]
 * 创建人: [Y.P]
 * 创建时间:[2017年7月16日 下午5:03:35]
 * 修改人: [Y.P]
 * 修改时间:[2017年7月16日 下午5:03:35]
 * 修改备注:[说明本次修改内容]
 * 版本:	 [v1.0]
 *
 */
package com.sa.service.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.sa.base.Manager;
import com.sa.base.ServerDataPool;
import com.sa.base.element.People;
import com.sa.base.element.Room;
import com.sa.net.Packet;
import com.sa.net.PacketHeadInfo;
import com.sa.net.PacketType;
import com.sa.util.Constant;


public class ClientResponecRoomRemove extends Packet {
	public ClientResponecRoomRemove(){}

	public ClientResponecRoomRemove(PacketHeadInfo packetHead) {
		this.setPacketHead(packetHead);
	}

	@Override
	public void execPacket() {
		try {
			/** 删除房间消息缓存*/
			ServerDataPool.dataManager.cleanLogs(this.getRoomId());
			
			/** 删除房间缓存*/
			Room removeRoom = ServerDataPool.dataManager.getRoom(this.getRoomId());
			this.setOption(2, JSON.toJSONString(removeRoom));
			Manager.INSTANCE.sendPacketToRoomAllUsers(this, Constant.CONSOLE_CODE_S);
			
			//用户是否在其他房间 若在不删IP和channel
			Map<String, People> peoplesMap = removeRoom.getPeoples();
			for (Entry<String, People> people : peoplesMap.entrySet()) {
				String userId = people.getKey();
				String userRoomNo = ServerDataPool.dataManager.getUserRoomNo(userId);
				if(null!=userRoomNo&&!"".equals(userRoomNo)){
					List<String> list = new ArrayList<String>(Arrays.asList(userRoomNo.split(",")));
					list.remove(this.getRoomId());
					if(list.size()>0){
						continue;
					}
				}
				//删user-serverip
				ServerDataPool.dataManager.delUserServer(userId);
			}
			ServerDataPool.dataManager.removeRoom(this.getRoomId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.ClientResponecRoomRemove;
	}
}
