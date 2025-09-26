const API_URL = '/fcgi';
const STORAGE_KEY = 'results_v2_green_fmt_fixedCircle_v2_xrange';

document.addEventListener('DOMContentLoaded', () => {
    const form        = document.getElementById('control-form');
    const xInput      = document.getElementById('x-input');
    const clearBtn    = document.getElementById('clear-history');
    const tableBody   = document.querySelector('#result-table tbody');

    // SVG layers (как раньше)
    const pointsLayer  = document.getElementById('points');
    const figuresLayer = document.getElementById('figures');
    const ticksLayer   = document.getElementById('ticks');
    const labelsLayer  = document.getElementById('labels');
    const minorGrid    = document.getElementById('grid-minor');
    const majorGrid    = document.getElementById('grid-major');

    let results = loadHistory();
    renderTable(results);
    redrawGeometry();

    document.getElementById('r-block').addEventListener('change', redrawGeometry);

    form.addEventListener('submit', (e) => {
        e.preventDefault();

        const rawX  = xInput.value.trim().replace(',', '.');   // сохраняем исходную форму (кроме запятой)
        const yVal  = getCheckedValue(form.elements['y']);
        const rVals = getCheckedList(form.elements['r']);

        const err = validate(rawX, yVal, rVals);
        if (err) return alert(err);

        const clientTime = new Date().toLocaleTimeString();

        const params = new URLSearchParams();
        params.append('x', rawX);          // ОТПРАВЛЯЕМ СТРОКУ БЕЗ ПРИНУДИТЕЛЬНОГО ОКРУГЛЕНИЯ
        params.append('y', yVal);
        rVals.forEach(rv => params.append('r', rv));
        params.append('clientTime', clientTime);

        fetch(API_URL + '?' + params.toString(), { method:'GET' })
            .then(r => r.text().then(t => ({ ok:r.ok, status:r.status, text:t })))
            .then(o => {
                if (!o.ok) throw new Error('HTTP ' + o.status + ': ' + o.text.slice(0,120));
                let json;
                try { json = JSON.parse(o.text); } catch { throw new Error('Невалидный JSON'); }
                if (json.error) return alert(json.error);
                if (!Array.isArray(json.results)) return alert('Нет массива results');

                json.results.slice().reverse().forEach(r => results.unshift(r));
                saveHistory(results);
                renderTable(results);
                redrawGeometry();
                plotNew(json.results);
            })
            .catch(e2 => alert('Ошибка: ' + e2.message));
    });

    clearBtn.addEventListener('click', () => {
        results = [];
        saveHistory(results);
        renderTable(results);
        pointsLayer.innerHTML = '';
    });

    /* ================== Валидация X ==================
       Работает чисто по строке, чтобы не съедать точность и не переходить к -5 */
    function validate(xStr, yVal, rList){
        if (!xStr) return 'Введите X';
        if (xStr.length > 32) return 'Слишком длинное число X';
        // Запрет экспоненциальной формы
        if (/[eE]/.test(xStr)) return 'Не используйте экспоненциальную форму';
        // Формат числа
        if (!/^[-+]?(\d+(\.\d*)?|\.\d+)$/.test(xStr)) return 'X не число';

        // Строчная проверка интервала (-5;5):
        // Отрицательный случай:
        //  - "-5" или "-5.x" -> вне
        //  - "-4..." -> ок
        // Положительный:
        //  - "5" или "5.000..." -> вне
        //  - "5.что-то" -> тоже вне (>=5)
        //  - "4.xxx" -> ок

        const s = xStr.startsWith('+') ? xStr.slice(1) : xStr; // убираем '+'
        if (s.startsWith('-')) {
            if (s === '-5' || s.startsWith('-5.')) return 'X вне диапазона (-5; 5)';
        } else {
            if (s === '5' || s.startsWith('5.')) return 'X вне диапазона (-5; 5)';
        }

        if (!yVal) return 'Выберите Y';
        if (!rList.length) return 'Выберите хотя бы один R';
        return null;
    }

    function getCheckedValue(list){
        if (!list) return null;
        if (list.length === undefined) return list.checked ? list.value : null;
        for (const el of list) if (el.checked) return el.value;
        return null;
    }
    function getCheckedList(list){
        const out = [];
        if (!list) return out;
        if (list.length === undefined){ if (list.checked) out.push(list.value); return out; }
        for (const el of list) if (el.checked) out.push(el.value);
        return out;
    }

    function loadHistory(){
        try {
            const raw = localStorage.getItem(STORAGE_KEY);
            if (!raw) return [];
            const arr = JSON.parse(raw);
            return Array.isArray(arr) ? arr : [];
        } catch { return []; }
    }
    function saveHistory(arr){ localStorage.setItem(STORAGE_KEY, JSON.stringify(arr)); }

    function fmt(n){
        if (typeof n !== 'number' || !isFinite(n)) return '';
        return Number(n.toFixed(4)).toString();
    }

    function renderTable(arr){
        tableBody.innerHTML = '';
        for (const row of arr){
            const tr = document.createElement('tr');
            tr.innerHTML = `
        <td>${fmt(row.x)}</td>
        <td>${fmt(row.y)}</td>
        <td>${fmt(row.r)}</td>
        <td style="color:${row.hit ? '#2dff9a':'#ff8070'};font-weight:600">${row.hit ? 'Да':'Нет'}</td>
        <td>${row.clientTime || ''}</td>
        <td>${row.execTimeNs ?? ''}</td>`;
            tableBody.appendChild(tr);
        }
        redrawPoints(arr);
    }

    function plotNew(arr){ arr.forEach(r => plotPoint(r.x, r.y, r.r, r.hit)); }

    /* ======== Графика (оставлена прежняя логика фигур/сетки) ======== */
    function redrawGeometry(){
        const rVals = getCheckedList(form.elements['r']).map(Number);
        const maxR  = rVals.length ? Math.max(...rVals) : 5;
        const limit = Math.max(6, maxR + 1);
        document.getElementById('plane').setAttribute('viewBox', `${-limit} ${-limit} ${limit*2} ${limit*2}`);
        buildGrid(limit);
        buildTicks(maxR);
        buildFigures(maxR);
        redrawPoints(results);
    }

    function buildGrid(limit){
        minorGrid.innerHTML=''; majorGrid.innerHTML='';
        for(let i=-limit;i<=limit;i++){
            const vl=line(i,-limit,i,limit), hl=line(-limit,i,limit,i);
            styleMinor(vl); styleMinor(hl);
            minorGrid.appendChild(vl); minorGrid.appendChild(hl);
        }
        for(let i=-limit;i<=limit;i++){
            const vl=line(i,-limit,i,limit), hl=line(-limit,i,limit,i);
            styleMajor(vl); styleMajor(hl);
            majorGrid.appendChild(vl); majorGrid.appendChild(hl);
        }
    }
    function styleMinor(l){ l.setAttribute('stroke','rgba(120,255,170,0.12)'); l.setAttribute('stroke-width','0.025'); }
    function styleMajor(l){ l.setAttribute('stroke','rgba(120,255,170,0.30)'); l.setAttribute('stroke-width','0.04'); }

    function buildTicks(maxR){
        ticksLayer.innerHTML=''; labelsLayer.innerHTML='';
        if(!maxR||!isFinite(maxR)) maxR=5;
        const values=[-maxR,-maxR/2,0,maxR/2,maxR];
        const used=new Set();
        values.forEach(v=>{
            const kx='x'+v, ky='y'+v;
            if(!used.has(kx)){
                addTick(v,0,'x');
                if(Math.abs(v)>1e-9) addLabel(v,-0.35,fmt(v));
                used.add(kx);
            }
            if(!used.has(ky)){
                addTick(0,v,'y');
                if(Math.abs(v)>1e-9) addLabel(0.35,v,fmt(v));
                used.add(ky);
            }
        });
        addLabel(maxR+0.45,0.5,'X');
        addLabel(0.5,maxR+0.7,'Y');
    }

    function buildFigures(r){
        figuresLayer.innerHTML='';
        if(!r||!isFinite(r)) r=5;
        const h=r/2;

        // Треугольник
        const tri=document.createElementNS('http://www.w3.org/2000/svg','polygon');
        tri.setAttribute('points',`0,0 ${h},0 0,${h}`);
        tri.setAttribute('class','fig-tri');
        figuresLayer.appendChild(tri);

        // Четверть окружности (аппроксимация)
        const segs=40;
        const pts=[];
        pts.push(`0,0`);
        pts.push(`-${h},0`);
        for(let i=1;i<=segs;i++){
            const t=(Math.PI/2)*(i/segs);
            const x=-h*Math.cos(t);
            const y= h*Math.sin(t);
            pts.push(`${x},${y}`);
        }
        const circ=document.createElementNS('http://www.w3.org/2000/svg','polygon');
        circ.setAttribute('points',pts.join(' '));
        circ.setAttribute('class','fig-circ');
        figuresLayer.appendChild(circ);

        // Прямоугольник
        const rect=document.createElementNS('http://www.w3.org/2000/svg','polygon');
        rect.setAttribute('points',`0,0 -${h},0 -${h},-${r} 0,-${r}`);
        rect.setAttribute('class','fig-rect');
        figuresLayer.appendChild(rect);
    }

    function plotPoint(x,y,r,hit){
        if(!isFinite(x)||!isFinite(y)) return;
        const c=document.createElementNS('http://www.w3.org/2000/svg','circle');
        c.setAttribute('cx',x);
        c.setAttribute('cy',-y);
        c.setAttribute('r',0.22);
        c.setAttribute('fill',hit?'#2dff9a':'#ff7b68');
        pointsLayer.appendChild(c);
    }

    function redrawPoints(arr){
        pointsLayer.innerHTML='';
        arr.forEach(row => plotPoint(row.x,row.y,row.r,row.hit));
    }

    // helpers
    function line(x1,y1,x2,y2){
        const l=document.createElementNS('http://www.w3.org/2000/svg','line');
        l.setAttribute('x1',x1); l.setAttribute('y1',y1);
        l.setAttribute('x2',x2); l.setAttribute('y2',y2);
        return l;
    }
    function addTick(x,y,axis){
        const len=0.18;
        const l=document.createElementNS('http://www.w3.org/2000/svg','line');
        if(axis==='x'){
            l.setAttribute('x1',x); l.setAttribute('x2',x);
            l.setAttribute('y1',-len); l.setAttribute('y2',len);
        } else {
            l.setAttribute('y1',y); l.setAttribute('y2',y);
            l.setAttribute('x1',-len); l.setAttribute('x2',len);
        }
        l.setAttribute('stroke','#b7ffe0');
        l.setAttribute('stroke-width','0.04');
        ticksLayer.appendChild(l);
    }
    function addLabel(x,y,text){
        const t=document.createElementNS('http://www.w3.org/2000/svg','text');
        t.setAttribute('x',x);
        t.setAttribute('y',-y);
        t.setAttribute('class','tick-label');
        t.textContent=text;
        labelsLayer.appendChild(t);
    }
});