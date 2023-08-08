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

            utility.ajax('/bej/plan', {title: postTitle ,planStartDate: tmpDate, color: postPlanColor}, 'POST')
                .then((responseData) => {
                    updatePlan(responseData);
                })
                .catch((error) => {
                    console.error('실패:', error);
                });
        },eventClick: function(info) {

            utility.ajax('/bej/planList', {info: 'info'}, 'post')
                .then((responseData) => {
                    plans = responseData.list;
                    let selectPlan = plans.find(plan => plan.planNo === info.event.extendedProps.no);
                    updateModal(selectPlan);
                    $('#myModal').modal('show'); // 모달에 내용 채워주기 이전에 모달을 띄웁니다.
                })
                .catch((error) => {
                    console.error('실패:', error);
                });

                //모달에 내용 채워주기
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
                            updatePlan(responseData);
                        })
                        .catch((error) => {
                            console.error('실패:', error);
                        });

                    $('#myModal').modal('hide');
                });

            $('#deleteBtn').on('click', function() {
                let selectedPlanNo = info.event.extendedProps.no;

                Swal.fire({
                    title: '계획 삭제',
                    text: '계획을 삭제하시겠습니까?',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: '삭제',
                    cancelButtonText: '취소'
                }).then((result) => {
                    if (result.isConfirmed) {
                        // 확인 버튼이 클릭되었을 때의 처리
                        utility.ajax('/bej/plan/', {planNo: selectedPlanNo}, 'delete')
                            .then((responseData) => {
                                updatePlan(responseData);
                            })
                            .catch((error) => {
                                console.error('계획 삭제 실패:', error);
                            });
                    }
                });

                $('#myModal').modal('hide'); // 모달 닫기
            });
        },eventDrop: function(info) {
            let selectedPlanNo = info.event.extendedProps.no;
            let newStartDate = new Date(info.event.start);
            let newEndDate = info.event.end ? new Date(info.event.end) : null; //

            newStartDate.setHours(newStartDate.getHours() + 9);
            if (newEndDate) {
                newEndDate.setHours(newEndDate.getHours() + newEndDate.getTimezoneOffset() / 60);
                newEndDate.setSeconds(newEndDate.getSeconds() - 1); // 1초를 빼주는 작업 추가
            }

            let sendData = {
                planNo: selectedPlanNo,
                planStartDate: newStartDate,
                planEndDate: newEndDate
            };

            utility.ajax('/bej/date', sendData, 'put')
                .then((responseData) => {
                    updatePlan(responseData);
                })
                .catch((error) => {
                    console.error('일정 이동 실패:', error);
                });
        },eventDragStop: function(info) {
                let calendarWrapper = $(info.view.calendar.el);

                if (!info.jsEvent.pageX) return;

                let mouseX = info.jsEvent.pageX;
                let mouseY = info.jsEvent.pageY;

                let calendarOffset = calendarWrapper.offset();
                let calendarTop = calendarOffset.top;
                let calendarLeft = calendarOffset.left;
                let calendarBottom = calendarTop + calendarWrapper.height();
                let calendarRight = calendarLeft + calendarWrapper.width();

            if (mouseX < calendarLeft || mouseX > calendarRight || mouseY < calendarTop || mouseY > calendarBottom) {
                let selectedPlanNo = info.event.extendedProps.no;

                // SweetAlert2를 사용하여 커스텀 디자인의 확인 대화 상자 표시
                Swal.fire({
                    title: '계획 삭제',
                    text: '계획을 삭제하시겠습니까?',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: '삭제',
                    cancelButtonText: '취소'
                }).then((result) => {
                    if (result.isConfirmed) {
                        // 확인 버튼이 클릭되었을 때의 처리
                        utility.ajax('/bej/plan/', {planNo: selectedPlanNo}, 'delete')
                            .then((responseData) => {
                                updatePlan(responseData);
                            })
                            .catch((error) => {
                                console.error('계획 삭제 실패:', error);
                            });
                    }
                });
            }

        },
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
            Swal.fire({
                icon: 'error',
                title: '오류',
                text: '동일한 키워드가 존재합니다.',
                confirmButtonText: '확인'
            });
            return;
        }
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

    $('#eventTitleInput').val('');
    $('#textArea').val('');
    $('#startDate').val('');
    $('#endDate').val('');
    $('#realStartDate').val('');
    $('#realEndDate').val('');
    $('#regDate').text('');
    $('#statusSelect').val(0);


    let title = plan.planTitle;
    let content = plan.planContent;
    let planStartDate = plan.planStartDate;
    let planEndDate = plan.planEndDate;
    let realStartDate = plan.realStartDate;
    let realEndDate = plan.realEndDate;
    let planStatus = plan.planStatus;
    let regDate = plan.modDate;

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
                updatePlan(responseData);
            })
            .catch((error) => {
                console.error('실패:', error);
            });

        $('#addModal').modal('hide');
    });


});

function updatePlan(responseData){

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
        if (plan.end) {
            let endDate = new Date(plan.end);
            endDate.setDate(endDate.getDate() + 1);
            plan.end = endDate;
        }
        calendar.addEvent(plan);
    });

    calendar.render();
}