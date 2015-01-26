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
-export([]).

start(ProgramName) ->
  spawn(?MODULE, init, [ProgramName]).

init(ProgramName) ->
  register(problem, self()),
  process_flag(trap_exit, true),
  Port = open_port({spawn, ProgramName}, [{packet, 2}]),
  loop(Port).

multiplicate(Num) ->
  call_port({multiplicate, Num}).
divide(Num) ->
  call_port({divide, Num}).

call_port(Msg) ->
  problem ! {call, self(), Msg},
  receive
    {problem, Result} -> Result
  end.

loop(Port) ->
  receive
    {call, Caller, Msg} ->
      Port ! {self(), {command, encode(Msg)}},
      receive
        {Port, {data, Data}} ->
          Caller ! {complex, decode(Data)}
      end,
      loop(Port)
  end.

encode({multiplicate, Num}) -> [1, Num];
encode({divide, Num}) -> [2, Num].

decode([Int]) -> Int.
