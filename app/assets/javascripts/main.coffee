class window.Choice
  constructor: (@row, @col, @option) ->
    @id = "##{@row}#{@col}-#{@option}"
    @cellId = "##{@row}#{@col}"
    @rowId = "#row#{@row}"
    @pick = "p#{@option}"
    @rowPick = "#{@row}-#{@pick}"

dismissedChoices = {}
picked = {}

# route pick/unpick actions taken on possible choices and chosen answers
routePick = (c) ->
  if dismissedChoices[c.id]
    undismissChoice c
  else if picked[c.rowPick]
    dupeAnswerPicked(c)
  else pickAnswer(c)

routeUnpick = (c) ->
  if picked[c.rowPick] == c.cellId
      unpickAnswer(c)
  else if picked[c.rowPick]
    dupeDismiss(c)
  else if dismissedChoices[c.id]
    undismissChoice c
  else
    dismissChoice c

# pick a possible choice as an answer / undo a previous answer pick
pickAnswer = (c) ->
  if checkClues(c)
    doAnswer c

doAnswer = (c) ->
  console.log(c.id + ": answer")
  picked[c.rowPick] = c.cellId
  $(c.cellId).removeClass("unpicked").addClass("picked "+c.pick)
  $(c.rowId).addClass(c.pick)

unpickAnswer = (c) ->
  unless prepickedAnswer(c)
    console.log(c.id + ": unanswer")
    picked[c.rowPick] = undefined
    $(c.cellId).addClass("unpicked").removeClass("picked "+c.pick)
    $(c.rowId).removeClass(c.pick)
dupeAnswerPicked = (c) ->
  console.log("already picked a #{c.pick} for row #{c.row}")
  highlightPicked(c)

# Dismiss/undismiss possible choices
dismissChoice = (c) ->
  console.log(c.id + ": dismiss")
  dismissedChoices[c.id] = true
  $(c.id).addClass "dismissed"
undismissChoice = (c) ->
  console.log(c.id + ": undismiss")
  dismissedChoices[c.id] = false
  $(c.id).removeClass "dismissed"
dupeDismiss = (c) ->
  console.log("choice #{c.id} already dismissed by picked answer: #{picked[c.rowPick]}")
  highlightPicked(c)

highlightPicked = (c) ->
  $(c.rowId+" .picked."+c.pick + "." + c.pick).effect("bounce")

checkClues = (c) -> true
prepickedAnswer = (c) -> false

dismissClue = (c) ->
  c.hide()

jQuery ->
  $(".choice").mousedown (e) ->
    e.preventDefault()
    p = $ this
    c = new Choice(p.parent().data('row'), p.parent().data('col'), p.data('pick'))
    if e.which is 1 then routePick(c) else routeUnpick(c)

  $(".choice").bind "contextmenu", (e) -> false

  $(".clue").bind "contextmenu", (e) ->
    e.preventDefault()
    dismissClue $ this
    false

  for choice in initialAnswers
    do (choice) -> doAnswer(choice)

