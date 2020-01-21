/**
 *
 * 项目名称:[NettyServer]
 * 包:	 [com.sa.service.server]
 * 类名称: [ServerRequestcShare]
 * 类描述: [修改共享]
 * 创建人: [Y.P]
 * 创建时间:[2017年7月11日 下午5:28:45]
 * 修改人: [Y.P]
 * 修改时间:[2017年7月11日 下午5:28:45]
 * 修改备注:[说明本次修改内容]
 * 版本:	 [v1.0]
 *
 */
package com.sa.service.server;

import java.util.HashMap;
import java.util.Map;

import com.sa.base.ConfManager;
import com.sa.base.Manager;
import com.sa.base.ServerDataPool;
import com.sa.net.Packet;
import com.sa.net.PacketType;
import com.sa.service.client.ClientMsgReceipt;
import com.sa.util.Constant;

public class ServerRequestbShareUpdSelf extends Packet {
	public ServerRequestbShareUpdSelf(){}

	@Override
	public PacketType getPacketType() {
		return PacketType.ServerRequestbShareUpdSelf;
	}

	@Override
	public void execPacket() {
		/** 根绝房间id 和 发信人id 校验用户角色 */
		//String userId = this.getFromUserId().replace("APP", "");
/*		String userId = this.getFromUserId();
		Set<String> checkRoleSet = new HashSet(){{add(Constant.ROLE_TEACHER);add(Constant.ROLE_PARENT_TEACHER);}};
		Map<String, Object> result = Permission.INSTANCE.checkUserRole(this.getRoomId(), userId,checkRoleSet);
		if (0 != ((Integer) result.get("code"))) {
			result = Permission.INSTANCE.checkUserAuth(this.getRoomId(), userId, (String) this.getOption(100));
		}*/

		Map<String, Object> result = new HashMap<>();
		result.put("code", 0);
		result.put("msg", "success");
		
		/** 实例化消息回执 并 赋值 并 执行 */
		new ClientMsgReceipt(this.getPacketHead(), result).execPacket();

		/** 如果校验成功 */
		if (0 == ((Integer) result.get("code"))) {
			/** 如果有中心 并 目标IP不是中心IP */
			if (ConfManager.getIsCenter() && !ConfManager.getCenterIp().equals(this.getRemoteIp())) {
				/** 转发消息到中心 */
				Manager.INSTANCE.sendPacketToCenter(this, Constant.CONSOLE_CODE_TS);
			} else {
				String[] roomIds = this.getRoomId().split(",");
				if (null != roomIds && roomIds.length > 0) {
					for (String rId : roomIds) {
						String shareK1 = (String) this.getOption(1);
						if (null != shareK1 && !"".equals(shareK1)) {
							synchronized (shareK1) {
								this.setRoomId(rId);
								setShare();
							}
						}
					}
				}
			}
		}
	}

	private int setShare() {
		String shareK = (String) this.getOption(1);
		String shareV = (String) this.getOption(2);
		String shareType = (String) this.getOption(3);
		String shareOptType = (String) this.getOption(4);
		String index = (String) this.getOption(5); // -1:从最后数;正数：从前面数
		String len = (String) this.getOption(6);
		String indexs = (String) this.getOption(7);
		String oldShareV = (String) this.getOption(8);
		
		int rs = 0;
		if ("del".equalsIgnoreCase(shareOptType)) {
			ServerDataPool.dataManager.removeShare(this.getRoomId(), shareK);
		} else if ("remove.1".equalsIgnoreCase(shareOptType)) {
			rs = ServerDataPool.dataManager.removeShare(this.getRoomId(), shareK, shareV);
		} else if ("remove.n.len".equalsIgnoreCase(shareOptType)) {
			rs = ServerDataPool.dataManager.removeShare(this.getRoomId(), shareK, Integer.parseInt(index),
					Integer.parseInt(len));
		} else if ("remove.n.index".equalsIgnoreCase(shareOptType)) {
			String[] arr = indexs.split(",");
			ServerDataPool.dataManager.removeShare(this.getRoomId(), shareK, arr);
		} else if ("upd".equalsIgnoreCase(shareOptType)) {
			/** 设置房间共享文件 */
			ServerDataPool.dataManager.setShare(this.getRoomId(), shareK, shareV, shareType);
		} else if ("upd.index".equalsIgnoreCase(shareOptType)) {
			/** 更新房间共享文件 */
			ServerDataPool.dataManager.updateShare(this.getRoomId(), shareK, shareV,Integer.parseInt(index));
		} else if ("upd.value".equalsIgnoreCase(shareOptType)) {
			/** 更新房间共享文件 */
			ServerDataPool.dataManager.updateShare(this.getRoomId(), shareK, oldShareV,shareV);
		}

		return rs;
	}

}