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

// 키워도 화면에 표시
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
    calendar = new Calendar(calendarEl, {
        timeZone : 'local',
        headerToolbar: {
            left  : 'prev,next',
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
            console.log("드래그");
            // 자주잡는 일정 -> 달력
            if (checkbox.checked) {
                // 체크돼있으면 일정을 달력에 옮기고 삭제됨
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

            let postTitle = info.draggedEl.innerText; //계획 title
            let postPlanStartDate = info.date; //계획 시작일
            let tmpDate = new Date(postPlanStartDate); //계획 시작일
            let postPlanColor = info.draggedEl.style.backgroundColor;
            tmpDate.setHours(tmpDate.getHours()+9);

            console.log('startDate');
            console.log(postPlanStartDate);
            utility.ajax('/bej/plan', {title: postTitle ,planStartDate: tmpDate, color: postPlanColor}, 'POST')
                .then((responseData) => {
                    console.log('일정 등록 성공:', responseData);
                    plans = responseData.list;
                    calendar.removeAllEvents();
                    planList = responseData.list.map((data) => ({
                        no: data.planNo,
                        title: data.planTitle,
                        start: new Date(data.planStartDate),
                        end: data.planEndDate == null ? new Date(data.planStartDate) : new Date(data.planEndDate),
                        allDay: true,
                        backgroundColor: data.planColor,
                        borderColor: data.planColor,
                    }));

                    planList.forEach((plan) => {
                        // planEndDate를 하루 늘림
                        if (plan.end) {
                            let endDate = new Date(plan.end);
                            endDate.setDate(endDate.getDate() + 1);
                            plan.end = endDate;
                        }
                        calendar.addEvent(plan);
                    });
                    calendar.render();
                })
                .catch((error) => {
                    console.error('실패:', error);
                });
        },eventClick: function(info) {

            utility.ajax('/bej/planList', {plan: 'plz'}, 'post')
                .then((responseData) => {
                    console.log('정보가져오기')
                    plans = responseData.list;
                    let selectPlan = plans.find(plan => plan.planNo === info.event.extendedProps.no);
                    updateModal(selectPlan);
                })
                .catch((error) => {
                    console.error('실패:', error);
                });
            //모달에 내용 채워주기
            $('#myModal').modal('show');
            $('#myModal').on('click', '.btn-primary', function() {

                let selectedPlanNo = info.event.extendedProps.no;

                let sendData = {
                    planNo: selectedPlanNo,
                    title:  $('#eventTitleInput').val(),
                    content: $('#textArea').val(),
                    planStartDate: $('#startDate').val(),
                    planEndDate: $('#endDate').val(),
                    realStartDate:  $('#realStartDate').val(),
                    realEndDate: $('#realEndDate').val(),
                    status: $('#statusSelect').val(), // 선택한 옵션 값 가져오기
                }

                // utility.ajax() 함수를 사용하여 서버로 데이터를 보냅니다.
                utility.ajax('/bej/plan', sendData, 'put')
                    .then((responseData) => {
                        console.log('일정 수정 성공:', responseData);
                        // 이벤트를 캘린더에서 모두 제거합니다.
                        calendar.removeAllEvents();

                        // 서버로부터 받은 데이터로 이벤트 리스트를 업데이트합니다.
                        planList = responseData.list.map((data) => ({
                            no: data.planNo,
                            title: data.planTitle,
                            start: new Date(data.planStartDate),
                            end: data.planEndDate == null ? new Date(data.planStartDate) : new Date(data.planEndDate),
                            allDay: true,
                            backgroundColor: data.planColor,
                            borderColor: data.planColor,
                        }));

                        planList.forEach((plan) => {
                            // planEndDate를 하루 늘림
                            if (plan.end) {
                                let endDate = new Date(plan.end);
                                endDate.setDate(endDate.getDate() + 1);
                                plan.end = endDate;
                            }
                            calendar.addEvent(plan);
                        });
                        calendar.render();
                    })
                    .catch((error) => {
                        console.error('실패:', error);
                    });

                $('#myModal').modal('hide');
            });
        },eventDrop: function (info) {

        }
    });

    planList.forEach((plan) => {
        // planEndDate를 하루 늘림
        if (plan.end) {
            let endDate = new Date(plan.end);
            endDate.setDate(endDate.getDate() + 1);
            plan.end = endDate;
        }
        calendar.addEvent(plan);
    });


    calendar.render();

    var currColor = '#3c8dbc' // 기본적으로 빨간색
    // 색상 선택 버튼
    $('#color-chooser > li > a').click(function (e) {
        e.preventDefault()
        currColor = $(this).css('color')
        $('#add-new-event').css({
            'background-color': currColor,
            'border-color'    : currColor
        })
    })

    $('#add-new-event').click(function (e) {
        e.preventDefault()
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

function updateModal(plan) {
    // 기존 코드를 초기화/삭제하세요
    $('#eventTitleInput').val('');
    $('#textArea').val('');
    $('#startDate').val('');
    $('#endDate').val('');
    $('#realStartDate').val('');
    $('#realEndDate').val('');
    $('#regDate').text('');
    $('#statusSelect').val(0);

    if (!plan) {
        return;
    }

    let title = plan.planTitle;
    let content = plan.planContent;
    let planStartDate = plan.planStartDate;
    let planEndDate = plan.planEndDate;
    let realStartDate = plan.realStartDate;
    let realEndDate = plan.realEndDate;
    let planStatus = plan.planStatus;
    let regDate = plan.modDate;

    // 여기서부터 기존 코드 사용
    $('#eventTitleInput').val(title);
    $('#textArea').val(content);
    $('#startDate').val(planStartDate);
    $('#endDate').val(planEndDate);
    $('#realStartDate').val(realStartDate);
    $('#realEndDate').val(realEndDate);
    $('#regDate').text('등록일: ' + formatDate(regDate));
    $('#statusSelect').val(planStatus);
}


function formatDate(date) {
    const formattedDate = new Date(date);
    const year = formattedDate.getFullYear();
    const month = String(formattedDate.getMonth() + 1).padStart(2, '0');
    const day = String(formattedDate.getDate()).padStart(2, '0');
    const hours = String(formattedDate.getHours()).padStart(2, '0');
    const minutes = String(formattedDate.getMinutes()).padStart(2, '0');
    const seconds = String(formattedDate.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}


$(document).ready(function () {

    $('.fc-color-picker a').on('click', function() {
        $('.fc-color-picker a.selected').removeClass('selected');
        $(this).addClass('selected');
        let selectedColor = $(this).css('color');
        $('#selectedColor').val(selectedColor);
    });


    $("#addPlanBtn").on("click", function () {
        $('#addTitle').val('');
        $('#selectedColor').val('');
        $('#addStartDate').val('');
        $('#addEndDate').val('');
        $('#addTextArea').val('');
        $('.fc-color-picker a.selected').removeClass('selected');

        $("#addModal").modal("show");
    });

    $('#newBtn').on('click', function() {

        const title = $('#addTitle').val();
        const selectedColor = $('#selectedColor').val();
        const startDate = $('#addStartDate').val();
        const endDate = $('#addEndDate').val();
        const content = $('#addTextArea').val();

        const data = {
            title: title,
            color: selectedColor,
            planStartDate: startDate,
            planEndDate: endDate,
            content: content
        };

        utility.ajax('/bej/plan', data, 'POST')
            .then((responseData) => {
                console.log('일정 등록 성공:', responseData);
                plans = responseData.list;
                calendar.removeAllEvents();
                planList = responseData.list.map((data) => ({
                    no: data.planNo,
                    title: data.planTitle,
                    start: new Date(data.planStartDate),
                    end: data.planEndDate == null ? new Date(data.planStartDate) : new Date(data.planEndDate),
                    allDay: true,
                    backgroundColor: data.planColor,
                    borderColor: data.planColor,
                }));

                planList.forEach((plan) => {
                    // planEndDate를 하루 늘림
                    if (plan.end) {
                        let endDate = new Date(plan.end);
                        endDate.setDate(endDate.getDate() + 1);
                        plan.end = endDate;
                    }
                    calendar.addEvent(plan);
                });


                calendar.render();
            })
            .catch((error) => {
                console.error('실패:', error);
            });

        $('#addModal').modal('hide');
    });

});