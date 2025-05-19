let stompClient = null;

const chatLineElementId = "chatLine";
const roomIdElementId = "roomId";
const messageElementId = "message";
const sendElementId = "send";
const magicRoomId = 1408;


const setConnected = (connected, roomId) => {
    const connectBtn = document.getElementById("connect");
    const disconnectBtn = document.getElementById("disconnect");

    connectBtn.disabled = connected;
    disconnectBtn.disabled = !connected;
    const chatLine = document.getElementById(chatLineElementId);
    chatLine.hidden = !connected;

    let isMagicRoom = (roomId == magicRoomId);
    const messageElement = document.getElementById(messageElementId);
    const sendElement = document.getElementById(sendElementId);
    messageElement.hidden = isMagicRoom;
    sendElement.hidden = isMagicRoom;
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        const userName = frame.headers["user-name"];
        const roomId = document.getElementById(roomIdElementId).value;
        setConnected(true, roomId);
        console.log(`Connected to roomId: ${roomId} frame:${frame}`);
        let topicName = `/topic/response.${roomId}`;
        if (roomId == magicRoomId) {
            topicName = `/topic/responseRoomAll`;
        }
        const topicNameUser = `/user/${userName}${topicName}`;
        stompClient.subscribe(topicNameUser, (message) => showMessage(JSON.parse(message.body).messageStr));
        stompClient.subscribe(topicName, (message) => showMessage(JSON.parse(message.body).messageStr));
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const sendMsg = () => {
    const roomId = document.getElementById(roomIdElementId).value;
    const message = document.getElementById(messageElementId).value;
    stompClient.send(`/app/message.${roomId}`, {}, JSON.stringify({'messageStr': message}))
}

const showMessage = (message) => {
    const chatLine = document.getElementById(chatLineElementId);
    let newRow = chatLine.insertRow(-1);
    let newCell = newRow.insertCell(0);
    let newText = document.createTextNode(message);
    newCell.appendChild(newText);
}
