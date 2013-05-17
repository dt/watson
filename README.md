# Watson

This is based on an old game I played in the '90s called Sherlock. I mostly wrote it to play with the Solver/Desolver code.

## Prerequisites
Play 2.1

### Installing Play 2.1
OSX:
    brew install play
or look at [http://www.playframework.com/documentation/2.1.1/Installing]

## Running
    play run

## Overview
Boards are generated on the server, by starting with a randomly generated full board and going through, eliminating random answers and adding clues in their place, confirming at each step that the game can be still be solved.

## Server Notes
* The server represents a game as a board and lists of each kind of clue.
* A board is a zero-indexed array of rows
* A row is a zero-indexed arrays of cells
* A cell is either a single answer or a list of possible answers.

## Frontend Notes
Using CoffeeScript and LESS for javascript and CSS. Both of these let, but don't force, you use much more concise syntaxes.

* Behavior is mostly in app/assets/javascripts/main.coffee
* css is currently split over three less files in app/assets/stylesheets: style.less, logic.less, main.less
    * main.less is more or less for layout
    * logic.less is mostly for nesting rules that hide/show options
    * style.less is for making everything purdy
* the board is 1-indexed. sorry.
