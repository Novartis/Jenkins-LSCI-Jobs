  def computer =hudson.model.User
  currentUser= []
  currentUser[0]=computer.current()
  return currentUser