package mfilaniu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import git4idea.commands.Git;
import git4idea.commands.GitCommand;
import git4idea.commands.GitCommandResult;
import git4idea.commands.GitLineHandler;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommitRenamer extends AnAction {

    static {
        System.out.println("CommitRenamer class loaded");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        GitRepositoryManager repoManager = GitRepositoryManager.getInstance(project);
        List<GitRepository> repositories = repoManager.getRepositories();
        if (repositories.isEmpty()) return;

        GitRepository repo = repositories.get(0);

        String newMessage = Messages.showInputDialog(
                project,
                "Enter new commit message:",
                "Rename Last Commit",
                Messages.getQuestionIcon()
        );
        if (newMessage == null || newMessage.trim().isEmpty()) return;

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            GitLineHandler handler = new GitLineHandler(project, repo.getRoot(), GitCommand.COMMIT);
            handler.addParameters("--amend", "-m", newMessage);

            GitCommandResult result = Git.getInstance().runCommand(handler);

            ApplicationManager.getApplication().invokeLater(() -> {
                if (result.success()) {
                    Messages.showInfoMessage(project, "Commit renamed successfully.", "Rename Last Commit");
                } else {
                    Messages.showErrorDialog(project,
                            "Failed to rename commit:\n" + result.getErrorOutputAsJoinedString(),
                            "Rename Last Commit");
                }

                repo.update();
            });
        });
    }

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        boolean enabled = project != null && ProjectLevelVcsManager.getInstance(project).checkVcsIsActive("Git");
        e.getPresentation().setEnabledAndVisible(enabled);
    }
}
