import Utility from '../utility.js';
const utility = new Utility();


//planList 만들기 타입맞춰주기
let planList = plans.map((data) => ({
    title: data.planTitle,
    start: new Date(data.planStartDate),
    end: data.dataplanEndDate == null ? new Date(data.planEndDate) : new Date(data.planStartDate),
    allDay: true,
    backgroundColor: data.planColor, //
    borderColor: data.planColor, //
    // 필요한 다른 필드들도 추가할 수 있습니다.
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

    console.log($(this).parent());
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

    /* initialize the calendar
     -----------------------------------------------------------------*/
    // 달력 이벤트를 위한 날짜 정보 (가짜 데이터)
    var date = new Date()
    var d    = date.getDate(),
        m    = date.getMonth(),
        y    = date.getFullYear()

    var Calendar = FullCalendar.Calendar;
    var Draggable = FullCalendar.Draggable;

    var containerEl = document.getElementById('external-events');
    var checkbox = document.getElementById('drop-remove');
    var calendarEl = document.getElementById('calendar');

    // 외부 이벤트를 초기화합니다.
    new Draggable(containerEl, {
        itemSelector: '.external-event',
        eventData: function(eventEl) {
            return {
                title: eventEl.innerText,
                backgroundColor: window.getComputedStyle(eventEl, null).getPropertyValue('background-color'),
                borderColor: window.getComputedStyle(eventEl, null).getPropertyValue('background-color'),
                textColor: window.getComputedStyle(eventEl, null).getPropertyValue('color'),
            };
        }
    },);

    // 달력을 초기화합니다.
    var calendar = new Calendar(calendarEl, {
        headerToolbar: {
            left  : 'prev,next today',
            center: 'title',
            right : 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        themeSystem: 'bootstrap',
        // 랜덤한 기본 이벤트들을 설정합니다.
        events: [

        ],
        editable  : true,      // 이벤트를 편집 가능하도록 합니다.
        droppable : true,      // 이벤트를 달력에 놓을 수 있도록 합니다.
        drop      : function(info) {
            // "드래그 후 삭제" 체크박스가 선택되었는지 확인합니다.
            if (checkbox.checked) {
                // 선택된 경우, 이벤트를 "Draggable Events" 목록에서 제거합니다.
                let deleteData = {keyword: info.draggedEl.innerText, color: info.draggedEl.style.backgroundColor};
                utility.ajax('/bej/keyword',deleteData,'delete')
                    .then((responseData) => {
                        console.log('키워드 제거 성공:', responseData);
                    })
                    .catch((error) => {
                        console.error('실패:', error);
                    });

                info.draggedEl.parentNode.removeChild(info.draggedEl);


            }
            // 이벤트가 달력에 놓여졌을 때의 동작을 여기에 추가합니다.
            // 이 부분에 원하는 알림 또는 동작을 작성하면 됩니다.

            let postTitle = info.draggedEl.innerText; //계획 title
            let postPlanStartDate = info.date; //계획 시작일
            let postPlanColor = info.draggedEl.style.backgroundColor;
            console.log(postPlanColor);

            utility.ajax('/bej/plan', {title: postTitle ,planStartDate: postPlanStartDate, color: postPlanColor}, 'POST')
                .then((responseData) => {
                    console.log('일정 등록 성공:', responseData);
                })
                .catch((error) => {
                    console.error('실패:', error);
                });
        }

    });


    planList.forEach((plan) => {
        calendar.addEvent(plan);
    });

    calendar.render();

    /* ADDING EVENTS */
    var currColor = '#3c8dbc' // 기본적으로 빨간색
    // 색상 선택 버튼
    $('#color-chooser > li > a').click(function (e) {
        e.preventDefault()
        // 색상 저장
        currColor = $(this).css('color')
        // 버튼에 색상 효과 추가
        $('#add-new-event').css({
            'background-color': currColor,
            'border-color'    : currColor
        })
    })

    // "새 이벤트 추가" 버튼 클릭 시
    $('#add-new-event').click(function (e) {
        e.preventDefault()
        // 값 가져오기 및 null이 아닌지 확인
        var val = $('#new-event').val()
        let texts = $('#external-events')[0].innerText;
        let textArray = texts.split('\n');

        console.log(textArray);

        if (val.length == 0) {
            return;
        } else if (textArray.includes(val)) {
            // 이미 등록된 텍스트인 경우, swal 띄우기
            Swal.fire({
                icon: 'error',
                title: '오류',
                text: '동일한 키워드가 존재합니다.',
                confirmButtonText: '확인'
            });
            return;
        }
        // 이벤트 생성
        var event = $('<div />')
        event.css({
            'background-color': currColor,
            'border-color'    : currColor,
            'color'           : '#fff'
        }).addClass('external-event')
        event.text(val)
        let deleteIcon = $('<i>').addClass('fas fa-times delete-icon float-right');
        event.append(deleteIcon);
        $('#external-events').prepend(event)

        // 드래그 기능 추가
        ini_events(event)

        utility.ajax('/bej/keyword',{title: event.text(),color: event.css('background-color')},'post')
            .then((responseData) => {
                console.log('성공:', responseData);
            })
            .catch((error) => {
                console.error('실패:', error);
            });

        // 텍스트 입력란에서 이벤트 삭제
        $('#new-event').val('')
    })
})
