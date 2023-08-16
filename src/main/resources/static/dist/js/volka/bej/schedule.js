import Utility from '../utility.js';
const utility = new Utility();

let calendar;

let planList = plans.map((data) => ({
    no: data.planNo,
    title: data.planTitle,
    start: new Date(data.planStartDate),
    end: data.planEndDate == null ? new Date(data.planStartDate) : new Date(data.planEndDate),
    allDay: true,
    backgroundColor: data.planColor,
    borderColor: data.planColor,
}));


//keyword 가져와서 화면에 뿌리기
let keyword = user.userKeyword;
let keywordList;
if (keyword) {
    keywordList = keyword.split('/');
    if (keywordList.length > 1) {
        keywordList.pop();
    }
} else {
    keywordList = [];
}

let eventContainer = $('#event-container');
for (let i = 0; i < keywordList.length; i++) {
    let eventData = keywordList[i].split('=');
    let eventText = eventData[0];
    let eventColor = eventData[1];
    let newEvent = $('<div>').addClass('external-event').text(eventText);
    newEvent.css('background-color', eventColor);
    newEvent.css('color', 'white');
    let deleteIcon = $('<i>').addClass('fas fa-times delete-icon float-right');
    newEvent.append(deleteIcon);
    eventContainer.append(newEvent);
}

$('#external-events').on('click', '.delete-icon', function () {
    // x 버튼 누를때 삭제 시키기
    $(this).parent().remove();
    let deleteData = {keyword: $(this).parent().text(), color: $(this).parent().css('background-color')};
    utility.ajax('/bej/keyword',deleteData,'delete')
        .then((responseData) => {
            console.log('키워드 제거 성공:', responseData);
        })
        .catch((error) => {
            console.error('실패:', error);
        });
});

$(function () {
    /* initialize the external events
     -----------------------------------------------------------------*/
    function ini_events(ele) {
        ele.each(function () {
            var eventObject = {
                title: $.trim($(this).text()) // 이벤트의 제목으로 요소의 텍스트를 사용합니다.
            }

            $(this).data('eventObject', eventObject)

            $(this).draggable({
                zIndex        : 1070,
                revert        : true, // 드래그 후 이벤트가 원래 위치로 되돌아가도록 합니다.
                revertDuration: 0     // 드래그 후 이벤트가 되돌아가는 데 걸리는 시간을 설정합니다.
            })
        })
    }

    // 외부 이벤트를 초기화합니다.
    ini_events($('#external-events div.external-event'))

    var date = new Date()
    var d    = date.getDate(),
        m    = date.getMonth(),
        y    = date.getFullYear()

    var Calendar = FullCalendar.Calendar;
    var Draggable = FullCalendar.Draggable;

    var containerEl = document.getElementById('external-events');
    var checkbox = document.getElementById('drop-remove');
    var calendarEl = document.getElementById('calendar');


    // 달력을 초기화합니다.
    calendar = new Calendar(calendarEl, {
        timeZone : 'local',
        headerToolbar: {
            left  : 'prev,next',
            center: 'title',
            right : 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        themeSystem: 'bootstrap',
        events: [
        ],
        editable  : false,
        droppable : false,
    });


    planList.forEach((plan) => {
        if (plan.end) {
            let endDate = new Date(plan.end);
            endDate.setDate(endDate.getDate() + 1);
            plan.end = endDate;
        }
        calendar.addEvent(plan);
    });


    calendar.render();
})



