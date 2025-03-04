package com.example.vidgram.model
data class UserChatInfo(
    var chatId: String = "",
    var lastMessage: String? = null,
    var timestamp: Long = 0L,
    val unreadCount: Int = 0,
    var senderId: String = "",  // Changed from user1Id to senderId
    var receiverId: String = "", // Changed from user2Id to receiverId
    var messages: List<ChatModel> = listOf() , // Optional: If you want to load all messages here,
    var receiverName : String?=""

) {
    fun messages(message: ChatModel) {

    }
}
