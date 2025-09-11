document.addEventListener('DOMContentLoaded', function() {
    fetch('/fragments/header')
        .then(response => response.text())
        .then(data => {
            document.getElementById('header').innerHTML = data;
        })
        .catch(error => console.error('헤더 로드 오류:', error));

    fetch('/fragments/footer')
        .then(response => response.text())
        .then(data => {
            document.getElementById('footer').innerHTML = data;
        })
        .catch(error => console.error('푸터 로드 오류:', error));
});


let chart; // 차트 인스턴스 재사용을 위해 전역에 선언
let candles = [];

// 초기 실행
const assetname = document.body.dataset.assetname
               || location.pathname.split('/').filter(Boolean).pop();
if (assetname) {
  fetchAndUpdateChart(assetname);

  // 🔄 1분(60,000ms)마다 갱신
  setInterval(() => fetchAndUpdateChart(assetname), 60000);
}

function fetchAndUpdateChart(assetname) {
// 데이터 가져오기
fetch(`/chart/${assetname}`)
  .then(r => {
    if (!r.ok) throw new Error(`서버 오류 (status: ${r.status})`);
    return r.json();
  })
  .then(payload => {
    const list = Array.isArray(payload?.pricesDtoList) ? payload.pricesDtoList : [];
    if (list.length === 0) {
      document.getElementById('empty').style.display = 'block';
      return;
    }

    // {x,o,h,l,c} 형태 변환
     candles = list.map(p => ({
      x: new Date(p.timestamp),
      o: Number(p.open),
      h: Number(p.high),
      l: Number(p.low),
      c: Number(p.close)
    }));

    const minX = candles[0].x.getTime();
    const maxX = candles[candles.length - 1].x.getTime();
    const rangeX = maxX - minX;
    const paddingX = rangeX * 0.05; // 5% 여유

    // Y축 범위 계산
    const minY = Math.min(...candles.map(c => c.l));
    const maxY = Math.max(...candles.map(c => c.h));
    const range = maxY - minY;
    const padding = range * 0.1
    const yMin = minY - padding;
    const yMax = maxY + padding;
    const ctx = document.getElementById('candle').getContext('2d');

    if (chart) {
      // 이미 차트가 있으면 데이터 갱신
      chart.data.datasets[0].data = candles;
      chart.options.scales.y.min = minY - 1000; // 여유 공간
      chart.options.scales.y.max = maxY + 1000;
      chart.update();
    } else {
      // 최초 차트 생성
      chart = new Chart(ctx, {
        type: 'candlestick',
        data: {
          datasets: [{
            label: (payload.assetsDto?.name || assetname) +
                   ' (' + (payload.assetsDto?.symbol || '') + ')',
            data: candles,
            barThickness: 8
          }]
        },
        options: {
          parsing: false,
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            x: {
              type: 'time',
              min: minX - paddingX,
              max: maxX + paddingX,
              time: { unit: 'minute', tooltipFormat: 'yyyy-MM-dd HH:mm' },
              ticks: { maxRotation: 0, autoSkip: true }
            },
           y: {
                min: yMin,
                max: yMax,
                ticks: { callback: v => v.toLocaleString('ko-KR') }
                }
          },
          plugins: {
            tooltip: {
              callbacks: {
                label: ctx => {
                  const v = ctx.raw;
                  return [
                    `O: ${v.o.toLocaleString('ko-KR')}`,
                    `H: ${v.h.toLocaleString('ko-KR')}`,
                    `L: ${v.l.toLocaleString('ko-KR')}`,
                    `C: ${v.c.toLocaleString('ko-KR')}`
                  ];
                }
              }
            }
          },
          layout: {
            padding: { top: 10, bottom: 10, left: 10, right: 10 }
          }
        }
      });
    }
  })
  .catch(err => {
    console.error("❌ fetch 실패:", err);
    const empty = document.getElementById('empty');
    empty.style.display = 'block';
    empty.textContent = '차트 데이터를 불러오지 못했습니다.';
  });
}