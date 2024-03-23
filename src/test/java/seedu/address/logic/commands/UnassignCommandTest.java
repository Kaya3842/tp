package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskList;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskList;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;
import seedu.address.testutil.PersonBuilder;

class UnassignCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalTaskList(), new UserPrefs());

    @Test
    public void execute_unassignTaskUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        Set<Task> editedTasks = new HashSet<>(firstPerson.getTasks());
        Task taskToUnassign = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());
        editedTasks.remove(taskToUnassign);
        Person editedPerson = new PersonBuilder(firstPerson).withTasks(editedTasks).build();

        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, INDEX_FIRST);

        String expectedMessage = String.format(UnassignCommand.MESSAGE_SUCCESS, Messages.formatTask(taskToUnassign),
                editedPerson.getName());

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new TaskList(model.getTaskList()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(unassignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        Set<Task> editedTasks = new HashSet<>(firstPerson.getTasks());
        Task taskToUnassign = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());
        editedTasks.remove(taskToUnassign);
        Person editedPerson = new PersonBuilder(firstPerson).withTasks(editedTasks).build();

        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, INDEX_FIRST);

        String expectedMessage = String.format(UnassignCommand.MESSAGE_SUCCESS, Messages.formatTask(taskToUnassign),
                editedPerson.getName());

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new TaskList(model.getTaskList()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(unassignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTaskIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1);
        UnassignCommand unassignCommand = new UnassignCommand(outOfBoundIndex, INDEX_FIRST);

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, outOfBoundIndex);

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST);
        Index outOfBoundIndex = INDEX_SECOND;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, outOfBoundIndex);

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, INDEX_FIRST);
        UnassignCommand unassignOneToTwoCommand = new UnassignCommand(INDEX_FIRST, INDEX_SECOND);
        UnassignCommand unassignTwoToOneCommand = new UnassignCommand(INDEX_SECOND, INDEX_FIRST);

        // same object -> returns true
        assertEquals(unassignCommand, unassignCommand);

        // same values -> returns true
        UnassignCommand unassignCommandCopy = new UnassignCommand(INDEX_FIRST, INDEX_FIRST);
        assertEquals(unassignCommand, unassignCommandCopy);

        // null -> returns false
        assertNotEquals(unassignCommand, null);

        // different indices -> returns false
        assertNotEquals(unassignCommand, unassignOneToTwoCommand);
        assertNotEquals(unassignCommand, unassignTwoToOneCommand);
    }
}
