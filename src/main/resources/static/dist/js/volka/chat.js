import Utility from './utility.js';
let utility = new Utility();

let nickName = user.userNickName;
$(document).ready(function () {
    console.log('연결');
    console.log(chats);
    console.log(user);
    console.log(chatRoom);
    renderChatRoom();
});

function renderChatRoom(){

    let ids = chatRoom.chatRoomParticipants.split('|');
    let roomName = '';

    for(let i = 0; i < ids.length - 1 ; i++){
        if(ids[i] != user.userId){
            roomName += chatRoom.participants[ids[i]];
        }
    }

    $(".chat-who").text(roomName+'님과의 채팅');
    $(".direct-chat-messages").empty();

    for(let chat of chats){
        let msgDiv = $('<div>').addClass('direct-chat-msg');
        let infoDiv = $('<div>').addClass('direct-chat-infos clearfix');
        let nameSpan = $('<span>').addClass('direct-chat-name').addClass(chat.chatUserId != user.userId ? 'float-left' : 'float-right').text(chatRoom.participants[chat.chatRoomNo]);
        let timeSpan = $('<span>').addClass('direct-chat-timestamp').addClass(chat.chatUserId != user.userId ? 'float-right' : 'float-left').text(chat.regDate);
        let img = $('<img>').addClass('direct-chat-img').attr('src', '/dist/img/volka.jpg').attr('alt', 'User Image');
        let textDiv = $('<div>').addClass('direct-chat-text').text(chat.chatContent);

        infoDiv.append(nameSpan).append(timeSpan);
        msgDiv.append(infoDiv).append(img).append(textDiv);

        if(chat.chatUserId == user.userId){
            msgDiv.addClass('right');
        }
        $(".direct-chat-messages").append(msgDiv);
        $(".direct-chat-messages").scrollTop($(".direct-chat-messages")[0].scrollHeight);
    }
}
