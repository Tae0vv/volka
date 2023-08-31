import Utility from './utility.js';
let utility = new Utility();

let nickName = user.userNickName;
$(document).ready(function () {
    renderChatRoom();
    history.replaceState({}, null, '/volka/chat');
    chatConnect();
    console.log(chats);
});

let stompChatClient = null;
function chatConnect() {
    let socket = new SockJS('/chat');
    stompChatClient = Stomp.over(socket);
    stompChatClient.connect({}, function(frame) {

        stompChatClient.subscribe('/queue/chat/' + nickName, function(msg) {
            chats = JSON.parse(msg.body);
            renderChatRoom();
        });
    },function (error){
        console.log(error);
    });
}


function renderChatRoom(){

    let ids = chatRoom.chatRoomParticipants.split('|');
    console.log(ids);
    let roomName = '';

    for(let i = 0; i < ids.length-1 ; i++){
        if(ids[i] != user.userId){
            roomName += chatRoom.participants[ids[i]];
        }
    }

    $(".chat-who").text(roomName+'님과의 채팅');
    $("#chat-room-no").val(chatRoom.chatRoomNo);
    $(".direct-chat-messages").empty();

    for(let chat of chats){

        let regDate = moment(chat.regDate, 'YYYY-MM-DDTHH:mm:ss.SSSSS').format('YYYY-MM-DD : HH:mm');

        let msgDiv = $('<div>').addClass('direct-chat-msg');
        let infoDiv = $('<div>').addClass('direct-chat-infos clearfix');
        let nameSpan = $('<span>').addClass('direct-chat-name').addClass(chat.chatUserId != user.userId ? 'float-left' : 'float-right').text(chatRoom.participants[chat.chatRoomNo]);
        let timeSpan = $('<span>').addClass('direct-chat-timestamp').addClass(chat.chatUserId != user.userId ? 'float-right' : 'float-left').text(regDate);
        let img = $('<img>').addClass('direct-chat-img').attr('alt', 'User Image');
        let textDiv = $('<div>').addClass('direct-chat-text').text(chat.chatContent);

        infoDiv.append(nameSpan).append(timeSpan);
        msgDiv.append(infoDiv).append(img).append(textDiv);

        if(chat.chatUserId == user.userId){
            msgDiv.addClass('right');
            img.attr('src', '/profile/image/' + user.userNickName);
        }else{
            img.attr('src', '/profile/image/' + roomName);
        }
        $(".direct-chat-messages").append(msgDiv);
        $(".direct-chat-messages").scrollTop($(".direct-chat-messages")[0].scrollHeight);
    }
    initEvent();
}

function initEvent(){
    $(document).off('click', '#chat-btn').on('click', '#chat-btn', function () {
        sendMessage();
        $("#chat-content").val('');
    });

    $(document).off('keydown', '#chat-content').on('keydown', '#chat-content', function (e) {
        if (e.keyCode == 13) {
            e.preventDefault();
            sendMessage();
            $("#chat-content").val('');
        }
    });
    $(document).off('click').on('click', function(event) {
        console.log('click');
        utility.ajax('/volka/read',{roomNo : chatRoom.chatRoomNo},'post')
            .then((responseData) => {
            })
            .catch((error) => {
            });
    });

}

function sendMessage(){
    let content = $("#chat-content").val();
    let ids = chatRoom.chatRoomParticipants.split('|');
    let memberNames = '';

    for(let i = 0; i < ids.length-1 ; i++){
        console.log('for');
        if(ids[i] != user.userId){
            memberNames += chatRoom.participants[ids[i]]+'|';
        }
    }
    if(content.trim() != ''){
        let data = {roomNo : $("#chat-room-no").val(), content : content, participants : memberNames}
        utility.ajax("/volka/chat",data, 'post')
            .then((responseData) => {
                console.log(responseData);
                chats = responseData.chats;
                renderChatRoom();
            })
            .catch((error) => {
                console.error(error);
            });
    }
}
