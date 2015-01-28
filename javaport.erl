%%%-------------------------------------------------------------------
%%% @author rafal_000
%%% @copyright (C) 2015, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 26. Jan 2015 12:25 PM
%%%-------------------------------------------------------------------
-module(javaport).
-author("rafal_000").

%% API
-export([start/1, init/1, multiplicate/1, divide/1, call_port/1]).

start(ProgramName) ->
  spawn(?MODULE, init, [ProgramName]). % spawnuje funkcje inicjalizujaco program

init(ProgramName) -> % inicjalizacja programu: zarejestrowanie procesu, otwarcie portu, loopowanie
  register(problem, self()),
  process_flag(trap_exit, true),
  Port = open_port({spawn, ProgramName}, [{packet, 2}]), % kluczowa linijka - otwarcie portu, pakiety 2-bajtowe
  loop(Port). % wejscie w petle

multiplicate(Num) -> % funkcja mnozaca wywolywane przez port
  call_port({multiplicate, Num}).
divide(Num) -> % funkcja dzielaca wywolywana przez port
  call_port({divide, Num}).

call_port(Msg) -> % wezwanie portu, wyslanie do procesu portowego podanej wiadomosci
  problem ! {call, self(), Msg},
  receive
    {problem, Result} -> Result % odebranie wyniku od procesu portowego
  end.

loop(Port) -> % port - nasluchuje wiadomosci i wywoluje odpowiednie wiadomosci
  receive
    {call, Caller, Msg} ->
      Port ! {self(), {command, encode(Msg)}}, % wyslanie do portu zakodowanego Msg
      receive
        {Port, {data, Data}} ->
          Caller ! {complex, decode(Data)} % wyslanie zdekodowanego wyniku
      end,
      loop(Port)
  end.

encode({multiplicate, Num}) -> [1, Num]; % proste enkodery funkcji
encode({divide, Num}) -> [2, Num].

decode([Int]) -> Int.     % prosty dekoder wyniku
